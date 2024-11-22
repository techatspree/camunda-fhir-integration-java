package de.akquinet.camunda.fhir;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = {"classpath:camunda_under_fhir_hapi.bpmn", "classpath:camunda_under_fhir_connector.bpmn"})
public class CamundaFhirIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaFhirIntegrationApplication.class, args);
	}

}
