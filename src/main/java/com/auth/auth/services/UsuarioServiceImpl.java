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
import com.auth.auth.repositories.DepartamentoRepository;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.repositories.RolRepository;
import com.auth.auth.repositories.UsuarioDepartamentosRepository;
import com.auth.auth.repositories.UsuarioRepository;
import com.auth.auth.services.interfaces.ApiServiceMail;
import com.auth.auth.services.interfaces.ApiServicePersona;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final DepartamentoRepository departamentoRepository;

    private final UsuarioDepartamentosRepository usuarioDepartamentosRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApiServicePersona apiServicePersona;

    private final ApiServiceMail apiServiceMail;

    private final PersonaRepository personaRepository;
    private final ApiProperties apiProperties;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            PersonaRepository personaRepository,
            ApiProperties apiProperties,
            DepartamentoRepository departamentoRepository,
            UsuarioDepartamentosRepository usuarioDepartamentosRepository,
            ApiServicePersona apiServicePersona,
            ApiServiceMail apiServiceMail) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.personaRepository = personaRepository;
        this.apiProperties = apiProperties;
        this.departamentoRepository = departamentoRepository;
        this.usuarioDepartamentosRepository = usuarioDepartamentosRepository;
        this.apiServicePersona = apiServicePersona;
        this.apiServiceMail = apiServiceMail;

    }

    @Override
    public List<UsuarioResponseList> findAll() {

        List<Usuario> response = usuarioRepository.findAll();

        return response.stream()
                .map(res -> {

                    UsuarioResponseList dto = new UsuarioResponseList();

                    PersonaResponse personaResponse = apiServicePersona
                            .getPersonaInfo(Integer.parseInt(res.getUsername()));

                    UsuarioDepartamentos usuarioDepartamentos = usuarioDepartamentosRepository.findByUsuario(res)
                            .orElse(null);

                    String nombre = personaResponse.getNombres() + " ";
                    String paterno = personaResponse.getPaterno() + " ";
                    String materno = personaResponse.getMaterno();

                    dto.setUsername(res.getUsername());
                    dto.setNombre(nombre.concat(paterno).concat(materno));
                    dto.setRut(personaResponse.getRut());
                    dto.setVrut(personaResponse.getVrut());
                    dto.setDepartamento(usuarioDepartamentos != null
                            ? usuarioDepartamentos.getDepartamento().getNombreDepartamento()
                            : null);

                    return dto;

                }).filter(dto -> dto.getDepartamento() != null)
                .toList();
    }

    @Override
    public UsuarioResponse createUser(Usuario usuario) {

        usuario.setRoles(getRolesForUser(usuario));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario.setActivationToken(usuario.generateActivationToken());

        int rut = Integer.parseInt(usuario.getUsername());

        Persona persona = personaRepository.findByRut(rut)
                .orElseGet(() -> personaRepository.save(new Persona(rut)));

        PersonaResponse personaResponse = apiServicePersona.getPersonaInfo(persona.getRut());
        usuario.setPersona(persona);

        sendMailActivation(usuario, personaResponse);

        usuario = usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getUsername(), usuario.getActivationToken());

    }

    private List<Rol> getRolesForUser(Usuario usuario) {
        List<Rol> roles = new ArrayList<>();
        rolRepository.findByName("ROLE_USER").ifPresent(roles::add);
        if (usuario.isAdmin())
            rolRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);
        if (usuario.isFunc())
            rolRepository.findByName("ROLE_FUNC").ifPresent(roles::add);
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

        Persona persona = personaRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));

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

        Departamento depto = departamentoRepository.findById(usuarioRequest.getIdDepto())
                .orElseThrow(() -> new IllegalArgumentException("El codigo de departamento no existe"));

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

                    usuarioDepartamentosRepository.save(usuarioDepartamentos);

                    return new UsuarioResponse(nuevoUsuario.getUsername());
                });
    }

    private List<Rol> getRolesForUserRequest(UsuarioRequest usuarioRequest) {
        List<Rol> roles = new ArrayList<>();
        rolRepository.findByName("ROLE_USER").ifPresent(roles::add);
        if (usuarioRequest.isAdmin())
            rolRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);
        if (usuarioRequest.isFunc())
            rolRepository.findByName("ROLE_FUNC").ifPresent(roles::add);
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

        Persona persona = personaRepository.findByRut(usuarioRequest.getRut()).orElse(null);

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
            persona = personaRepository.save(persona);
        }

        if (optionalPersonaResponse.isPresent() && persona == null) {
            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaRepository.save(persona);
        }

        return persona;
    }

    @Override
    public UsuarioResponse getUsuario(String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario"));

        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setUsername(usuario.getUsername());

        return usuarioResponse;
    }

    @Override
    public Usuario getUsuarioByPersona(Persona persona) {

        return usuarioRepository.findByPersona(persona)
                .orElseThrow(
                        () -> new IllegalArgumentException("Usuario no encontrado para el RUT: " + persona.getRut()));
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

}
