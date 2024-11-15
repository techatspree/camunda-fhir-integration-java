package de.akquinet.camunda.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HapiFHIRContextProvider {

    @Bean
    public FhirContext createFhirContext() {
        return FhirContext.forR4();
    }
}
