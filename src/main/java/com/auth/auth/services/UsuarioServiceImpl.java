package com.auth.auth.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
import com.auth.auth.mail.EmailService;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.repositories.RolRepository;
import com.auth.auth.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final ApiService apiService;

    private final PersonaRepository personaRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder, EmailService emailService,
            ApiService apiService, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.apiService = apiService;
        this.personaRepository = personaRepository;

    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public UsuarioResponse save(Usuario usuario) {
        Optional<Rol> optRol = rolRepository.findByName("ROLE_USER");
        List<Rol> roles = new ArrayList<>();

        optRol.ifPresent(roles::add);

        if (usuario.isAdmin()) {
            Optional<Rol> optRolAdmin = rolRepository.findByName("ROLE_ADMIN");
            optRolAdmin.ifPresent(roles::add);
        }

        if (usuario.isFunc()) {
            Optional<Rol> optRolAdmin = rolRepository.findByName("ROLE_FUNC");
            optRolAdmin.ifPresent(roles::add);
        }

        usuario.setRoles(roles);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario.setActivationToken(usuario.generateActivationToken());

        Persona persona = new Persona(Integer.parseInt(usuario.getUsername()));

        PersonaResponse personaResponse = apiService.obtenerDatos(persona.getRut());

        personaRepository.save(persona);

        usuario.setPersona(persona);

        String activationLink = "https://dev.appx.cl/api/auth/usuarios/activate?token="
                + usuario.getActivationToken();
        emailService.sendMail(personaResponse.getEmail(), "Activa tu cuenta",
                "Hola " + usuario.getUsername() + ", activa tu cuenta con el siguiente enlace: " + activationLink);

        usuario = usuarioRepository.save(usuario);

        UsuarioResponse usuarioResponse = new UsuarioResponse();

        usuarioResponse.setUsername(usuario.getUsername());
        usuarioResponse.setActivationToken(usuario.getActivationToken());

        return usuarioResponse;

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

        String activationLink = "https://dev.appx.cl/api/auth/usuarios/activate?token=" + usuario.getActivationToken();
        emailService.sendMail(email, "Cambio de correo ", activationLink);

    }

    @Override
    public UsuarioResponse saveUserFunc(UsuarioRequest usuarioRequest) {

    // Obtener usuario si ya existe en la BD
    Usuario usuario = usuarioRepository.findByUsername(usuarioRequest.getRut().toString()).orElse(null);

    // Obtener roles y asignarlos según corresponda
    List<Rol> roles = new ArrayList<>();
    rolRepository.findByName("ROLE_USER").ifPresent(roles::add);

    if (usuarioRequest.isAdmin()) {
        rolRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);
    }

    if (usuarioRequest.isFunc()) {
        rolRepository.findByName("ROLE_FUNC").ifPresent(roles::add);
    }

    // Si el usuario ya existe, solo actualizar roles y devolver respuesta
    if (usuario != null) {
        usuario.getRoles().addAll(roles); // Agregar los nuevos roles
        usuario.setRoles(new ArrayList<>(new HashSet<>(usuario.getRoles()))); // Evitar duplicados
        usuarioRepository.save(usuario);

        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setUsername(usuario.getUsername());
        return usuarioResponse;
    }

    // Buscar persona en la API
    PersonaResponse personaResponse = apiService.obtenerDatos(usuarioRequest.getRut());
    Persona persona = personaRepository.findByRut(usuarioRequest.getRut()).orElse(null);

    if (personaResponse == null) {
        // Si la persona NO existe en la BD local, crearla
        if (persona == null) {
            PersonaRequest personaRequest = new PersonaRequest();
            personaRequest.setRut(usuarioRequest.getRut());
            personaRequest.setVrut(usuarioRequest.getVrut());
            personaRequest.setNombres(usuarioRequest.getNombres());
            personaRequest.setPaterno(usuarioRequest.getPaterno());
            personaRequest.setMaterno(usuarioRequest.getMaterno());
            personaRequest.setEmail(usuarioRequest.getEmail());

            apiService.crearPersona(personaRequest); // Se crea en la API

            // También la guardamos en la BD local
            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaRepository.save(persona);
        }
    } else {
        // Si la API sí devuelve datos y la persona no está en la BD, guardarla en la BD
        if (persona == null) {
            persona = new Persona();
            persona.setRut(usuarioRequest.getRut());
            persona = personaRepository.save(persona);
        }
    }

    // Crear nuevo usuario con la persona encontrada o creada
    usuario = new Usuario();
    usuario.setUsername(usuarioRequest.getRut().toString());
    usuario.setPassword(passwordEncoder.encode(usuarioRequest.getPassword()));
    usuario.setRoles(roles);
    usuario.setPersona(persona);
    usuario.setEnabled(true);

    usuario = usuarioRepository.save(usuario);

    // Respuesta
    UsuarioResponse usuarioResponse = new UsuarioResponse();
    usuarioResponse.setUsername(usuario.getUsername());

    return usuarioResponse;
}

    
    @Override
    public UsuarioResponse buscarUsuario(String username){

        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("No existe el usuario"));

        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setUsername(usuario.getUsername());

        return usuarioResponse;
    }

}
