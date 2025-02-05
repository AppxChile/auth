package com.auth.auth.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaResponse;
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
        this.emailService= emailService;
        this.apiService = apiService;
        this.personaRepository = personaRepository;

    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    

    @Override
    public Usuario save(Usuario usuario) {
        Optional<Rol> optRol = rolRepository.findByName("ROLE_USER");
        List<Rol> roles = new ArrayList<>();

        optRol.ifPresent(roles::add);

        if (usuario.isAdmin()) {
            Optional<Rol> optRolAdmin = rolRepository.findByName("ROLE_ADMIN");
            optRolAdmin.ifPresent(roles::add);
        }

        usuario.setRoles(roles);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuario.setActivationToken(usuario.generateActivationToken());

        Persona persona = new Persona(Integer.parseInt(usuario.getUsername()));

        PersonaResponse personaResponse = apiService.obtenerDatos(persona.getRut());

        personaRepository.save(persona);

        usuario.setPersona(persona);

                String activationLink = "http://localhost:8080/api/usuarios/activate?token=" + usuario.getActivationToken();
        emailService.sendMail(personaResponse.getEmail(), "Activa tu cuenta",
                "Hola " + usuario.getUsername() + ", activa tu cuenta con el siguiente enlace: " + activationLink);



        return usuarioRepository.save(usuario);
    }

}


