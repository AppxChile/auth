package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Persona;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.services.interfaces.PersonaService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public Persona getPersonaByRut(Integer rut) {
        return RepositoryUtils.findOrThrow(personaRepository.findByRut(rut),
                String.format("Person con el rut %d no encontrada", rut));
    }

    @Override
    public Persona save(Persona persona) {
        return personaRepository.save(persona);
    }

}
