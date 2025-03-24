package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Persona;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.services.interfaces.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public Persona getPersonaByRut(Integer rut) {
        return personaRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada" + rut));
    }

}
