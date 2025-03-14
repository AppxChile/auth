package com.auth.auth.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;
import com.auth.auth.configuration.ApiProperties;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClientPersona;

    private final WebClient webClientMail;

    public ApiService(WebClient.Builder webClientBuilder, ApiProperties apiProperties) {
        this.webClientPersona = webClientBuilder.baseUrl(apiProperties.getPersonaUrl()).build();
        this.webClientMail = webClientBuilder.baseUrl(apiProperties.getMailUrl()).build();
    }

    public PersonaResponse getPersonaInfo(Integer rut) {
        return webClientPersona.get()
                .uri("/{rut}", rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PersonaResponse.class)
                .onErrorResume(Exception.class, e -> Mono.empty())
                .block();
    }

    public void createPersona(PersonaRequest persona) {

        try {
            webClientPersona.post()
                    .uri("/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(persona)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new RuntimeException("Error en la API: " + error))))
                    .bodyToMono(Void.class) //
                    .block();

        } catch (WebClientResponseException e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
        }

    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            webClientMail.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/send")
                            .queryParam("to", to)
                            .queryParam("subject", subject)
                            .queryParam("templateName", templateName)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(variables) // JSON con variables para la plantilla
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> response.bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new RuntimeException("Error en la API: " + error))))
                    .bodyToMono(Void.class)
                    .block();

        } catch (WebClientResponseException e) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
        }
    }

}
