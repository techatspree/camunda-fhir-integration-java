package de.akquinet.camunda.fhir.handler;

import de.akquinet.camunda.fhir.service.PatientData;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.VariablesAsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendPatientDataToSAPHandler {
    private final static Logger LOG = LoggerFactory.getLogger(SendPatientDataToSAPHandler.class);

    @JobWorker(type = "send-patientdata_to_sap")
    public void setPatientAddressToSAP(@VariablesAsType PatientData variables) {
        LOG.info("sending patient data to SAP: {}", variables);
    }
}
