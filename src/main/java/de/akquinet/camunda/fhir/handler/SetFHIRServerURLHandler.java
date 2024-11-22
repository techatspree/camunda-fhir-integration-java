package de.akquinet.camunda.fhir.handler;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SetFHIRServerURLHandler {
    private final static Logger LOG = LoggerFactory.getLogger(SetFHIRServerURLHandler.class);

    private final String serverUrl;

    public SetFHIRServerURLHandler(@Value("${hapi.fhir.serverbase}") String serverURL) {
        this.serverUrl = serverURL;
    }

    @JobWorker(type = "set-fhir-server-url")
    public Map<String, String> setFHIRServerURL() {
        LOG.info("setting FHIR server URL: {}", serverUrl);

        return Map.of("fhirServerUrl", serverUrl);
    }
}
