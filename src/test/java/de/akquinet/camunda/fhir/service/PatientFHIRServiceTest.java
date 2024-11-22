package de.akquinet.camunda.fhir.service;

import de.akquinet.camunda.fhir.WiremockTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"hapi.fhir.serverbase=http://localhost:8081/"})
public class PatientFHIRServiceTest extends WiremockTestBase {

    @Autowired
    private PatientFHIRService underTest;

    @Test
    public void testGetPatient() {
        PatientData patientData = new PatientData();
        patientData.setPatientId("example");
        PatientData completedPatientData = underTest.getPatientData(patientData);
        Assertions.assertThat(completedPatientData.getPatientId()).isEqualTo("example");
    }
}
