package com.auth.auth.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaResponse;
import com.auth.auth.entities.PasswordResetToken;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;
import com.auth.auth.mail.EmailService;
import com.auth.auth.repositories.PasswordResetTokenRepository;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.repositories.UsuarioRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class PasswordRecoveryService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PersonaRepository personaRepository;
    private final ApiService apiService;

    public PasswordRecoveryService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            PasswordResetTokenRepository tokenRepository,
            PersonaRepository personaRepository,
            ApiService apiService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
        this.personaRepository = personaRepository;
        this.apiService = apiService;
    }

    public void sendRecoveryEmail(Integer rut) {

        Persona persona = personaRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada" + rut));

        // Buscar al usuario por RUT
        Usuario usuario = usuarioRepository.findByPersona(persona)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado para el RUT: " + rut));

        // Generar un token único y temporal
        String token = UUID.randomUUID().toString();

        // Guardar el token en la base de datos
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, usuario);
        tokenRepository.save(passwordResetToken);

        // Crear un enlace de recuperación
        String recoveryLink = "http://dev.appx.cl/api/auth/usuarios/recovery?token=" + token;

        PersonaResponse personaResponse = apiService.obtenerDatos(rut);


        Map<String, Object> variables = Map.of(
                "nombre", personaResponse.getNombres(),
                "recoveryLink", recoveryLink);

        try {
            emailService.sendHtmlEmail(personaResponse.getEmail(), "Recuperacion de contraseña", "recovery-template", variables);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o expirado"));

        // Paso 2: Verificar la fecha de expiración del token
        if (passwordResetToken.isExpired()) {
            throw new IllegalArgumentException("El token ha expirado");
        }

        // Paso 3: Actualizar la contraseña del usuario asociado al token
        Usuario usuario = passwordResetToken.getUsuario();

        // Codificar la nueva contraseña
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        usuarioRepository.save(usuario);

        // Paso 4: Eliminar el token usado
        tokenRepository.delete(passwordResetToken);

        // Mensaje final
    }

}
