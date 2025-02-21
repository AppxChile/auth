package com.auth.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.services.PasswordRecoveryService;


@RestController
@RequestMapping("/auth/api/usuarios")
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
    public ResponseEntity<Object> resetPassword(@RequestParam String token, @RequestBody String newPassword) {
        try {
            // Verificar si el token es válido
            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest().body("El token es obligatorio.");
            }
    
            // Verificar si la contraseña es válida
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("La nueva contraseña es obligatoria.");
            }
    
            // Intentar restablecer la contraseña
            passwordRecoveryService.resetPassword(token, newPassword);
    
            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } catch (IllegalArgumentException e) {
            // Si el servicio lanza una excepción por token inválido
            return ResponseEntity.status(400).body("El token proporcionado no es válido.");
        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            return ResponseEntity.status(500).body("Error interno del servidor. Por favor, intente más tarde.");
        }
    }
    
}