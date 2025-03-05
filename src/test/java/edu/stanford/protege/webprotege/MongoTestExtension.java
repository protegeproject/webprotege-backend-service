package edu.stanford.protege.webprotege;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.Properties;

public class MongoTestExtension implements BeforeAllCallback, AfterAllCallback {

    private static final Logger logger = LoggerFactory.getLogger(MongoTestExtension.class);
    private MongoDBContainer mongoDBContainer;
    private String mongoDbName;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        loadMongoDbName();

        var imageName = DockerImageName.parse("mongo");
        mongoDBContainer = new MongoDBContainer(imageName)
                .withExposedPorts(27017, 27017);
        mongoDBContainer.start();

        String mongoUri = mongoDBContainer.getReplicaSetUrl(mongoDbName);
        logger.info("MongoDB started at {}", mongoUri);

        System.setProperty("spring.data.mongodb.uri", mongoUri);
    }

    private void loadMongoDbName() {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application-test.properties"));
            mongoDbName = properties.getProperty("spring.data.mongodb.database", "webprotege");
        } catch (IOException e) {
            logger.warn("Could not load application-test.properties, using default MongoDB name: {}", mongoDbName);
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        mongoDBContainer.stop();
    }
}
