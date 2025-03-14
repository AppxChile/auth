package com.auth.auth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

    private String personaUrl;
    private String mailUrl;
    private String activationUrl;
    private String recoveryUrl;

    public String getPersonaUrl() {
        return personaUrl;
    }

    public void setPersonaUrl(String personaUrl) {
        this.personaUrl = personaUrl;
    }

    public String getMailUrl() {
        return mailUrl;
    }

    public void setMailUrl(String mailUrl) {
        this.mailUrl = mailUrl;
    }

    public String getActivationUrl() {
        return activationUrl;
    }

    public void setActivationUrl(String activationUrl) {
        this.activationUrl = activationUrl;
    }

    public String getRecoveryUrl() {
        return recoveryUrl;
    }

    public void setRecoveryUrl(String recoveryUrl) {
        this.recoveryUrl = recoveryUrl;
    }
}
