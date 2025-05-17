package com.auth.auth.services.interfaces;

import com.auth.auth.api.PersonaRequest;
import com.auth.auth.api.PersonaResponse;

public interface ApiServicePersona {

    PersonaResponse getPersonaInfo(Integer rut);

    void createPersona(PersonaRequest persona);

}
