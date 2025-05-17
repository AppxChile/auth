package com.auth.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.PasswordRequest;
import com.auth.auth.services.interfaces.PasswordRecoveryService;

@RestController
@RequestMapping("/api/auth/usuarios")
@CrossOrigin(origins = { "https://dev.appx.cl/", "http//localhost:5173" })
public class PasswordController {

    private final PasswordRecoveryService passwordRecoveryService;

    public PasswordController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/recover")
    public ResponseEntity<Object> recoverPassword(@RequestParam Integer rut) {
        try {
            passwordRecoveryService.sendRecoveryEmail(rut);
            return ResponseEntity.ok("Correo enviado exitosamente");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("El parámetro 'rut' debe ser un número válido.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestParam String token, @RequestBody PasswordRequest newPassword) {
        try {
            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest().body("El token es obligatorio.");
            }

            if (newPassword == null || newPassword.getNewPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("La nueva contraseña es obligatoria.");
            }

            passwordRecoveryService.resetPassword(token, newPassword);

            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("El token proporcionado no es válido.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor. Por favor, intente más tarde.");
        }
    }

}