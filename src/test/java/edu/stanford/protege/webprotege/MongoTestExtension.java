package edu.stanford.protege.webprotege;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-06-06
 */
public class MongoTestExtension implements BeforeAllCallback, AfterAllCallback {

    private static Logger logger = LoggerFactory.getLogger(MongoTestExtension.class);

    private MongoDBContainer mongoDBContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var imageName = DockerImageName.parse("mongo");
        mongoDBContainer = new MongoDBContainer(imageName)
                .withExposedPorts(27017, 27017);
        mongoDBContainer.start();

        var mappedHttpPort = mongoDBContainer.getMappedPort(27017);
        logger.info("MongoDB port 27017 is mapped to {}", mappedHttpPort);
        System.setProperty("spring.data.mongodb.port", Integer.toString(mappedHttpPort));

    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        mongoDBContainer.stop();
    }
}
