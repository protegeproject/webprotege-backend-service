package edu.stanford.protege.webprotege;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-06-06
 */
public class RabbitTestExtension implements BeforeAllCallback, AfterAllCallback {

    private static Logger logger = LoggerFactory.getLogger(RabbitTestExtension.class);

    private RabbitMQContainer rabbitContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var imageName = DockerImageName.parse("rabbitmq:3.7.25-management-alpine");
        rabbitContainer = new RabbitMQContainer(imageName)
                .withExposedPorts(5672);
        rabbitContainer.start();

        System.setProperty("spring.rabbitmq.host", rabbitContainer.getHost());
        System.setProperty("spring.rabbitmq.port", String.valueOf(rabbitContainer.getAmqpPort()));
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        rabbitContainer.stop();
    }
}
