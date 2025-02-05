package com.auth.auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.auth.auth.api.PersonaResponse;

@Service
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder, @Value("${api.persona.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public PersonaResponse obtenerDatos(Integer rut) {
        return webClient.get()
                .uri("/{rut}", rut) // Se agrega el RUT a la URL
                .retrieve()
                .bodyToMono(PersonaResponse.class)
                .block(); // Bloquea hasta recibir la respuesta (sincrónico)
    }
}
