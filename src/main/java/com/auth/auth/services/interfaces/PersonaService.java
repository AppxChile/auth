package com.auth.auth.services.interfaces;

import com.auth.auth.entities.Persona;

public interface PersonaService {

    Persona getPersonaByRut(Integer rut);

    Persona save(Persona persona);

}
