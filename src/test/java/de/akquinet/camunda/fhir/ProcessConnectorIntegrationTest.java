package de.akquinet.camunda.fhir;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@SpringBootTest
public class ProcessConnectorIntegrationTest {
    @Autowired
    private ZeebeClient client;

    @Test
    public void testDeploymentAndStartProcessInstance() {
        ProcessInstanceEvent processInstance = client
                .newCreateInstanceCommand()
                .bpmnProcessId("camundafhirconnector")
                .latestVersion()
                .variable("patientId", "example")
                .send()
                .join();


        RestClient restClient = RestClient.create();
        restClient
            .post()
            .uri("http://localhost:8080/api/login?username=demo&password=demo")
            .retrieve();

        Awaitility
                .await()
                .atMost(Duration.ofSeconds(30))
                .until(() -> checkForProcessInstanceIsCompleted(restClient, processInstance.getProcessInstanceKey()));
    }

    private boolean checkForProcessInstanceIsCompleted(RestClient restClient, long processInstanceKey) {
        ResponseEntity<String> result = restClient
                .post()
                .uri("http://localhost:8080/v1/process-instances/search")
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\n" +
                        "    \"filter\": { \n" +
                        "        \"key\": " + processInstanceKey +
                        "    }\n" +
                        "}")
                .retrieve()
                .toEntity(String.class);
        return result.getStatusCode().is2xxSuccessful() && result.getBody().contains("COMPLETED");
    }
}
