package com.auth.auth.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaResponse;

@Service
public class PersonaService {

    private final ApiService apiService;

    public PersonaService(ApiService apiService) {
        this.apiService = apiService;
    }

    public PersonaResponse getPersonaa(Integer rut) {

        PersonaResponse personaResponse = apiService.getPersonaInfo(rut);

        Optional<PersonaResponse> optionalPersonaResponse = Optional.ofNullable(personaResponse);

        if (optionalPersonaResponse.isEmpty()) {
            throw new IllegalArgumentException("No existe el rut");
        }

        return personaResponse;

    }

}
