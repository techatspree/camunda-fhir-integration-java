package de.akquinet.camunda.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PatientFHIRServiceTest {

    private PatientFHIRService underTest = new PatientFHIRService(FhirContext.forR4(), "https://hapi.fhir.org/baseR4");

    @BeforeEach
    public void setUp() {
        underTest = new PatientFHIRService(FhirContext.forR4(), "https://hapi.fhir.org/baseR4");
    }

    @Test
    public void testGetPatient() {
        PatientData patientData = new PatientData();
        patientData.setPatientId("example");
        PatientData completedPatientData = underTest.getPatientData(patientData);
        Assertions.assertThat(completedPatientData.getPatientId()).isEqualTo("example");
    }
}
