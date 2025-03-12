package com.auth.auth.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;
import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Rol;
import com.auth.auth.entities.Usuario;
import com.auth.auth.exceptions.SendMailExceptions;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.repositories.RolRepository;
import com.auth.auth.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApiService apiService;

    private final PersonaRepository personaRepository;

    private final String urlActivation;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            ApiService apiService, PersonaRepository personaRepository,
            @Value("${api.activation.url}") String urlActivation) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.apiService = apiService;
        this.personaRepository = personaRepository;
        this.urlActivation = urlActivation;

    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public UsuarioResponse save(Usuario usuario) {

        usuario.setRoles(getRolesForUser(usuario));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario.setActivationToken(usuario.generateActivationToken());

        int rut = Integer.parseInt(usuario.getUsername());

        Persona persona = personaRepository.findByRut(rut)
                .orElseGet(() -> personaRepository.save(new Persona(rut)));

        PersonaResponse personaResponse = apiService.getPersonaInfo(persona.getRut());
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
        String activationLink = urlActivation + usuario.getActivationToken();
        Map<String, Object> variables = Map.of("nombre", personaResponse.getNombres(), "link", activationLink);

        try {
            apiService.sendEmail(personaResponse.getEmail(), "Activa tu registro", "register-template", variables);
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

        String activationLink = urlActivation + usuario.getActivationToken();

        Map<String, Object> variables = Map.of(
                "nombre", usuario.getUsername(),
                "codigo", activationLink);

        try {
            apiService.sendEmail(email, "Correo con Thymeleaf", "email-template", variables);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public UsuarioResponse saveUserFunc(UsuarioRequest usuarioRequest) {

        List<Rol> roles = getRolesForUserRequest(usuarioRequest);

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
        PersonaResponse personaResponse = apiService.getPersonaInfo(usuarioRequest.getRut());

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

            apiService.createPersona(personaRequest);

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

}
