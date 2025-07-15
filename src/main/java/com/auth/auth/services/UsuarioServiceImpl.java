package com.auth.auth.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;
import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.dto.UsuarioResponseList;
import com.auth.auth.entities.Departamento;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Rol;
import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioDepartamentos;
import com.auth.auth.exceptions.SendMailExceptions;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.ApiServiceMail;
import com.auth.auth.services.interfaces.ApiServicePersona;
import com.auth.auth.services.interfaces.DepartamentoService;
import com.auth.auth.services.interfaces.PersonaService;
import com.auth.auth.services.interfaces.RolService;
import com.auth.auth.services.interfaces.UsuarioDepartamentosService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDepartamentosService usuarioDepartamentosService;

    private final UsuarioRepository usuarioRepository;

    private final DepartamentoService departamentoService;

    private final PersonaService personaService;

    private final RolService rolService;

    private final PasswordEncoder passwordEncoder;

    private final ApiServicePersona apiServicePersona;

    private final ApiServiceMail apiServiceMail;

    private final ApiProperties apiProperties;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            ApiProperties apiProperties,
            ApiServicePersona apiServicePersona,
            ApiServiceMail apiServiceMail,
            UsuarioDepartamentosService usuarioDepartamentosService,
            DepartamentoService departamentoService,
            RolService rolService,
            PersonaService personaService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.apiProperties = apiProperties;
        this.apiServicePersona = apiServicePersona;
        this.apiServiceMail = apiServiceMail;
        this.usuarioDepartamentosService = usuarioDepartamentosService;
        this.departamentoService = departamentoService;
        this.rolService = rolService;
        this.personaService = personaService;

    }

    @Override
    public List<UsuarioResponseList> findAll() {

        List<Usuario> response = usuarioRepository.findAll();

        return response.stream()
                .map(res -> {

                    UsuarioResponseList dto = new UsuarioResponseList();

                    PersonaResponse personaResponse = apiServicePersona
                            .getPersonaInfo(Integer.parseInt(res.getUsername()));

                    UsuarioDepartamentos usuarioDepartamentos = usuarioDepartamentosService.findByUsuario(res);

                    dto.setUsername(res.getUsername());
                    dto.setNombre(personaResponse.getNombreCompleto());
                    dto.setRut(personaResponse.getRut());
                    dto.setVrut(personaResponse.getVrut());
                    dto.setIdDepto(usuarioDepartamentos != null
                            ? usuarioDepartamentos.getNombreDepartamento()
                            : null);

                    return dto;

                }).filter(dto -> dto.getIdDepto() != null)
                .toList();
    }

    @Override
    public UsuarioResponse createUser(Usuario usuario) {

        usuario.setRoles(getRolesForUser(usuario));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario.setActivationToken(usuario.generateActivationToken());

        int rut = Integer.parseInt(usuario.getUsername());

        Persona persona = personaService.getPersonaByRut(rut);

        PersonaResponse personaResponse = apiServicePersona.getPersonaInfo(persona.getRut());
        usuario.setPersona(persona);

        sendMailActivation(usuario, personaResponse);

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getUsername(), usuario.getActivationToken());

    }

    private List<Rol> getRolesForUser(Usuario usuario) {
        List<Rol> roles = new ArrayList<>();
        rolService.getByName("ROLE_USER").ifPresent(roles::add);
        if (usuario.isAdmin())
            rolService.getByName("ROLE_ADMIN").ifPresent(roles::add);
        if (usuario.isFunc())
            rolService.getByName("ROLE_FUNC").ifPresent(roles::add);
        return roles;
    }

    private void sendMailActivation(Usuario usuario, PersonaResponse personaResponse) {
        String activationLink = apiProperties.getActivationUrl() + usuario.getActivationToken();
        Map<String, Object> variables = Map.of("nombre", personaResponse.getNombres(), "link", activationLink);

        try {
            apiServiceMail.sendEmail(personaResponse.getEmail(), "Activa tu registro", "register-template", variables);
        } catch (SendMailExceptions e) {
            throw new SendMailExceptions("Error enviando correo de activación a " + personaResponse.getEmail());
        }
    }

    @Override
    public void changeMail(ChangeMailRequest request) {

        Integer rut = request.getRut();
        String email = request.getEmail();

        Persona persona = personaService.getPersonaByRut(rut);

        Usuario usuario = usuarioRepository.findByPersona(persona).orElseThrow();

        usuario.setEnabled(false);
        usuario.setActivationToken(usuario.generateActivationToken());

        usuarioRepository.save(usuario);

        String activationLink = apiProperties.getActivationUrl() + usuario.getActivationToken();

        Map<String, Object> variables = Map.of(
                "nombre", usuario.getUsername(),
                "codigo", activationLink);

        try {
            apiServiceMail.sendEmail(email, "Correo con Thymeleaf", "email-template", variables);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public UsuarioResponse createUserFunc(UsuarioRequest usuarioRequest) {

        List<Rol> roles = getRolesForUserRequest(usuarioRequest);

        Departamento depto = departamentoService.getById(usuarioRequest.getIdDepto());

        return usuarioRepository.findByUsername(usuarioRequest.getRut().toString())
                .map(usuarioExistente -> {
                    updateRoles(usuarioExistente, roles);
                    return new UsuarioResponse(usuarioExistente.getUsername());
                })
                .orElseGet(() -> {
                    Persona persona = getOrCreatePersonaFromApi(usuarioRequest);

                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setUsername(usuarioRequest.getRut().toString());
                    nuevoUsuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
                    nuevoUsuario.setRoles(roles);
                    nuevoUsuario.setPersona(persona);
                    nuevoUsuario.setEnabled(true);

                    nuevoUsuario = usuarioRepository.save(nuevoUsuario);

                    UsuarioDepartamentos usuarioDepartamentos = new UsuarioDepartamentos(nuevoUsuario, depto);

                    usuarioDepartamentosService.save(usuarioDepartamentos);

                    return new UsuarioResponse(nuevoUsuario.getUsername());
                });
    }

    private List<Rol> getRolesForUserRequest(UsuarioRequest usuarioRequest) {
        List<Rol> roles = new ArrayList<>();
        rolService.getByName("ROLE_USER").ifPresent(roles::add);
        if (usuarioRequest.isAdmin())
            rolService.getByName("ROLE_ADMIN").ifPresent(roles::add);
        if (usuarioRequest.isFunc())
            rolService.getByName("ROLE_FUNC").ifPresent(roles::add);
        return roles;
    }

    private void updateRoles(Usuario usuario, List<Rol> newRoles) {
        Set<Rol> uniqueRoles = new HashSet<>(usuario.getRoles());
        uniqueRoles.addAll(newRoles);
        usuario.setRoles(new ArrayList<>(uniqueRoles));
        usuarioRepository.save(usuario);
    }

    private Persona getOrCreatePersonaFromApi(UsuarioRequest usuarioRequest) {
        PersonaResponse personaResponse = apiServicePersona.getPersonaInfo(usuarioRequest.getRut());

        Optional<PersonaResponse> optionalPersonaResponse = Optional.ofNullable(personaResponse);

        Persona persona = personaService.getPersonaByRut(usuarioRequest.getRut());

        if (optionalPersonaResponse.isEmpty() && persona == null) {
            PersonaRequest personaRequest = new PersonaRequest();
            personaRequest.setRut(usuarioRequest.getRut());
            personaRequest.setVrut(usuarioRequest.getVrut());
            personaRequest.setNombres(usuarioRequest.getNombres());
            personaRequest.setPaterno(usuarioRequest.getPaterno());
            personaRequest.setMaterno(usuarioRequest.getMaterno());
            personaRequest.setEmail(usuarioRequest.getEmail());

            apiServicePersona.createPersona(personaRequest);

            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaService.save(persona);
        }

        if (optionalPersonaResponse.isPresent() && persona == null) {
            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaService.save(persona);
        }

        return persona;
    }

    @Override
    public UsuarioResponse getUsuario(String username) {

        Usuario usuario = RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format("Usuario %s no encontrad0", username));

        UsuarioDepartamentos usuarioDepartamentos = usuarioDepartamentosService.findByUsuario(usuario);

        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setUsername(usuario.getUsername());
        usuarioResponse.setIdDepartamento(usuarioDepartamentos.getDepartamento().getId());

        return usuarioResponse;
    }

    @Override
    public Usuario getUsuarioByPersona(Persona persona) {

        return RepositoryUtils.findOrThrow(usuarioRepository.findByPersona(persona),
                String.format("Persona con el rut %d no encontrada", persona.getRut()));

    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario findByUsername(String username) {

        return RepositoryUtils.findOrThrow(usuarioRepository.findByUsername(username),
                String.format("Usuairo %s no encontrado", username));

    }

}
