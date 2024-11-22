package de.akquinet.camunda.fhir;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@DirtiesContext
@EnableWireMock(@ConfigureWireMock(port = 8081))
abstract public class WiremockTestBase {

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    public void setup() throws IOException {
        stubFor(
                get("/metadata")
                        .willReturn(okJson(
                                resourceLoader.getResource("classpath:/metadata-response.json").getContentAsString(Charset.defaultCharset())))
        );
        stubFor(
                get("/Patient/example")
                        .willReturn(okJson(
                                resourceLoader.getResource("classpath:/hapi-fhir-response.json").getContentAsString(Charset.defaultCharset())))
        );
    }

}
