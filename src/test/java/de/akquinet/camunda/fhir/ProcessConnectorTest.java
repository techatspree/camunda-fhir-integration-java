package de.akquinet.camunda.fhir;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.DeploymentEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.assertions.BpmnAssert;
import io.camunda.zeebe.process.test.assertions.DeploymentAssert;
import io.camunda.zeebe.process.test.assertions.ProcessInstanceAssert;
import io.camunda.zeebe.process.test.extension.ZeebeProcessTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@ZeebeProcessTest
public class ProcessConnectorTest {
    private ZeebeTestEngine engine;
    private ZeebeClient client;

    @Test
    public void testDeploymentAndStartProcessInstance() {
        DeploymentEvent deploymentEvent = deployProcessDef("camunda_under_fhir_connector.bpmn");
        DeploymentAssert assertions = BpmnAssert.assertThat(deploymentEvent);
        assertions.containsProcessesByBpmnProcessId("camundafhirconnector");
    }

    @Test
    public void testStartProcessInstance() throws InterruptedException, TimeoutException {
        deployProcessDef("camunda_under_fhir_connector.bpmn");
        ProcessInstanceEvent event = client.newCreateInstanceCommand()
                .bpmnProcessId("camundafhirconnector")
                .latestVersion()
                .send()
                .join();

        completeServiceTasks("set-fhir-server-url", 1);
        completeServiceTasks("io.camunda:http-json:1", 1);
        completeServiceTasks("send-patientdata_to_sap", 1);

        ProcessInstanceAssert assertions = BpmnAssert.assertThat(event);
        assertions.hasPassedElementsInOrder("StartEvent", "getPatientData", "setPatientData", "EndEvent");
        assertions.isCompleted();
    }

    private DeploymentEvent deployProcessDef(String processDefinition) {
        return client.newDeployResourceCommand()
                .addResourceFromClasspath(processDefinition)
                .send()
                .join();

    }

    private void completeServiceTasks(final String jobType, final int count)
            throws InterruptedException, TimeoutException {

        final var activateJobsResponse =
                client.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(count).send().join();

        final int activatedJobCount = activateJobsResponse.getJobs().size();
        if (activatedJobCount < count) {
            Assertions.fail(
                    "Unable to activate %d jobs, because only %d were activated."
                            .formatted(count, activatedJobCount));
        }

        for (int i = 0; i < count; i++) {
            final var job = activateJobsResponse.getJobs().get(i);

            client.newCompleteCommand(job.getKey()).send().join();
        }

        engine.waitForIdleState(Duration.ofSeconds(1));
    }

}
