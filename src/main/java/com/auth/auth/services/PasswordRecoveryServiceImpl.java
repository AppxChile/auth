package com.auth.auth.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaResponse;
import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.entities.PasswordResetToken;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;
import com.auth.auth.exceptions.SendMailExceptions;
import com.auth.auth.services.interfaces.ApiServiceMail;
import com.auth.auth.services.interfaces.ApiServicePersona;
import com.auth.auth.services.interfaces.PasswordRecoveryService;
import com.auth.auth.services.interfaces.PasswordResetTokenService;
import com.auth.auth.services.interfaces.PersonaService;

@Service
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

    private final PasswordEncoder passwordEncoder;
    private final ApiServiceMail apiServiceMail;
    private final ApiServicePersona apiService;
    private final ApiProperties apiProperties;
    private final PersonaService personaService;
    private final UsuarioService usuarioService;
    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordRecoveryServiceImpl(
            PasswordEncoder passwordEncoder,
            PasswordResetTokenService passwordResetTokenService,
            ApiServiceMail apiServiceMail,
            ApiServicePersona apiService,
            ApiProperties apiProperties,
            PersonaService personaService,
            UsuarioService usuarioService) {
        this.passwordEncoder = passwordEncoder;
        this.apiServiceMail = apiServiceMail;
        this.apiProperties = apiProperties;
        this.apiService = apiService;
        this.personaService = personaService;
        this.usuarioService=usuarioService;
        this.passwordResetTokenService=passwordResetTokenService;
    }

    @Override
    public void sendRecoveryEmail(Integer rut) {
        Persona persona = personaService.getPersonaByRut(rut);

        // Buscar al usuario por RUT
        Usuario usuario = usuarioService.getUsuarioByPersona(persona);

        // Generar un token único y temporal
        String token = UUID.randomUUID().toString();

        // Guardar el token en la base de datos
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, usuario);
        passwordResetTokenService.save(passwordResetToken);

        // Crear un enlace de recuperación
        String recoveryLink = apiProperties.getRecoveryUrl() + token;

        PersonaResponse personaResponse = apiService.getPersonaInfo(rut);

        Map<String, Object> variables = Map.of(
                "nombre", personaResponse.getNombres(),
                "recoveryLink", recoveryLink);

        try {
            apiServiceMail.sendEmail(personaResponse.getEmail(), "Recuperacion de contraseña", "recovery-template",
                    variables);
        } catch (SendMailExceptions e) {
            throw new SendMailExceptions("Error enviando correo de activación a " + personaResponse.getEmail());
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.getByToken(token);
              
        if (passwordResetToken.isExpired()) {
            throw new IllegalArgumentException("El token ha expirado");
        }

        Usuario usuario = passwordResetToken.getUsuario();

        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        usuarioService.save(usuario);

        passwordResetTokenService.delete(passwordResetToken);

    }

}
