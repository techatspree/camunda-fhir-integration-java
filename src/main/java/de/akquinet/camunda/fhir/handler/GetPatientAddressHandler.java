package de.akquinet.camunda.fhir.handler;

import de.akquinet.camunda.fhir.service.PatientData;
import de.akquinet.camunda.fhir.service.PatientFHIRService;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetPatientAddressHandler {
    private final static Logger LOG = LoggerFactory.getLogger(GetPatientAddressHandler.class);

    @Autowired
    private PatientFHIRService patientService;

    @JobWorker(type = "get-patient-address")
    public PatientData getPatientAddress(@VariablesAsType PatientData patientData) {
        LOG.info("patientId: {}", patientData.getPatientId());

        return patientService.getPatientData(patientData);
    }
}
