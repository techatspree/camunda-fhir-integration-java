package de.akquinet.camunda.fhir;

import io.camunda.process.test.api.CamundaSpringProcessTest;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.camunda.process.test.api.CamundaAssert.assertThat;

@SpringBootTest
@CamundaSpringProcessTest
public class ProcessHapiIntegrationTest {
    @Autowired
    private ZeebeClient client;

    @Test
    public void testDeploymentAndStartProcessInstance() {
        ProcessInstanceEvent processInstance = client
                .newCreateInstanceCommand()
                .bpmnProcessId("camundafhirhapi")
                .latestVersion()
                .variable("patientId", "example")
                .send()
                .join();

        assertThat(processInstance)
                .hasVariable("patientId", "example")
                .hasVariable("city", "PleasantVille")
                .hasVariable("state", "Vic")
                .hasVariable("zip", "3999")
                .hasVariable("street", "534 Erewhon St")
                .hasVariable("use", "HOME")
                .isCompleted();
    }

}
