package com.auth.auth.services.interfaces;

import java.util.Map;

public interface ApiServiceMail {

    void sendEmail(String to, String subject, String templateName, Map<String, Object> variables);

}
