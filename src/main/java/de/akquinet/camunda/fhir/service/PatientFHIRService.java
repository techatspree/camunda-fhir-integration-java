package de.akquinet.camunda.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class PatientFHIRService {

    private final static Logger LOG = LoggerFactory.getLogger(PatientFHIRService.class);

    private final FhirContext fhirContext;
    private final String serverBase;

    public PatientFHIRService(FhirContext fhirContext, @Value("${hapi.fhir.serverbase}") String serverBase) {
        this.fhirContext = fhirContext;
        this.serverBase = serverBase;
    }

    public PatientData getPatientData(PatientData patientData) {
        // Create a client
        IGenericClient client = fhirContext.newRestfulGenericClient(serverBase);

        // Read a patient with the given ID
        Patient patient = client.read().resource(Patient.class).withId(patientData.getPatientId()).execute();

        // Print the output
        String string = fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
        LOG.info("Patient from FHIR: {}", string);

        Address firstAddress = patient.getAddressFirstRep();
        patientData.setCity(firstAddress.getCity());
        patientData.setCountry(firstAddress.getCountry());
        patientData.setState(firstAddress.getState());
        patientData.setZip(firstAddress.getPostalCode());
        patientData.setStreet(firstAddress.getLine().stream().map(PrimitiveType::getValue).collect(Collectors.joining()));
        patientData.setUse(firstAddress.getUse().name());

        return patientData;
    }
}
