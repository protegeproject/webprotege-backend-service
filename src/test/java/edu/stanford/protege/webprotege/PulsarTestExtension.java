package edu.stanford.protege.webprotege;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-06-06
 */
public class PulsarTestExtension implements BeforeAllCallback, AfterAllCallback {

    private static Logger logger = LoggerFactory.getLogger(PulsarTestExtension.class);

    private PulsarContainer pulsarContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var imageName = DockerImageName.parse("apachepulsar/pulsar");
        pulsarContainer = new PulsarContainer(imageName)
                .withCommand("standalone")
                .withExposedPorts(6650, 8080);
        pulsarContainer.start();

        var mappedHttpPort = pulsarContainer.getMappedPort(8080);
        logger.info("Pulsar port 8080 is mapped to {}", mappedHttpPort);
        System.setProperty("webprotege.pulsar.serviceHttpUrl", "http://localhost:" + mappedHttpPort);
        var mappedPort = pulsarContainer.getMappedPort(6650);
        logger.info("Pulsar port 6650 is mapped to {}", mappedPort);
        System.setProperty("webprotege.pulsar.serviceUrl", "pulsar://localhost:" + mappedPort);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        pulsarContainer.stop();
    }
}
