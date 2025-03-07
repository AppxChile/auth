package com.auth.auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;

import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient.Builder webClientBuilder, @Value("${api.persona.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

public PersonaResponse obtenerDatos(Integer rut) {
    return webClient.get()
            .uri("/{rut}", rut)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
            .bodyToMono(PersonaResponse.class)
            .onErrorResume(Exception.class, e -> Mono.empty())
            .block(); 
}

    public void crearPersona(PersonaRequest persona){

         try {
            webClient.post()
                    .uri("/create") 
                    .contentType(MediaType.APPLICATION_JSON) 
                    .bodyValue(persona) 
                    .retrieve()
                    .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Error en la API: " + error)))
                    )
                    .bodyToMono(Void.class) // 
                    .block(); 

        } catch (WebClientResponseException e) {
            System.err.println("Error en changeMail: " + e.getResponseBodyAsString());
        }



    }
}
