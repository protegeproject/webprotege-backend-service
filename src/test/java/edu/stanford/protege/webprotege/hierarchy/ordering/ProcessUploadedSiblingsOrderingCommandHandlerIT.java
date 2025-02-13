package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.RabbitTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ExtendWith({SpringExtension.class, MongoTestExtension.class, RabbitTestExtension.class})
@ActiveProfiles("test")
public class ProcessUploadedSiblingsOrderingCommandHandlerIT {

    @Autowired
    private ProcessUploadedSiblingsOrderingCommandHandler commandHandler;

    @SpyBean
    private OrderedChildrenDocumentService orderedChildrenDocumentService;

    @SpyBean
    private MinioFileDownloader minioFileDownloader;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String testJsonContent;
    private final String documentId = "test-document-id";
    private final ProjectId projectId = new ProjectId(UUID.randomUUID().toString());

    @BeforeEach
    void setUp() throws IOException {
        mongoTemplate.dropCollection(EntityChildrenOrdering.class);

        String jsonFilePath = "src/test/resources/orderedSiblingsTest.json";
        testJsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        Mockito.doAnswer(invocation -> new ByteArrayInputStream(testJsonContent.getBytes()))
                .when(minioFileDownloader).fetchDocument(anyString());

    }

    @Test
    public void GIVEN_uploadedDocument_WHEN_handleRequestCalled_THEN_batchesAreProcessedAndPersisted() {
        var request = new ProcessUploadedSiblingsOrderingAction(projectId, new DocumentId(documentId));
        var executionContext = new ExecutionContext();

        var response = commandHandler.handleRequest(request, executionContext).block();

        assertNotNull(response, "Response should not be null");

        List<EntityChildrenOrdering> persistedChildren = mongoTemplate.findAll(EntityChildrenOrdering.class);
        assertFalse(persistedChildren.isEmpty(), "Processed children should be saved to MongoDB");

        for (EntityChildrenOrdering child : persistedChildren) {
            Query query = new Query(Criteria.where(EntityChildrenOrdering.ENTITY_URI).is(child.entityUri()));
            long count = mongoTemplate.count(query, EntityChildrenOrdering.class);
            assertTrue(count > 0, "Data should be persisted in MongoDB");
        }

        Mockito.verify(orderedChildrenDocumentService, Mockito.times(1)).fetchFromDocument(documentId);
    }

    @Test
    public void GIVEN_uploadedDocument_WHEN_handleRequestCalledTwice_THEN_noDuplicatesAreCreated() {
        var request = new ProcessUploadedSiblingsOrderingAction(projectId, new DocumentId(documentId));
        var executionContext = new ExecutionContext();

        commandHandler.handleRequest(request, executionContext).block();

        long initialCount = mongoTemplate.count(new Query(), EntityChildrenOrdering.class);

        commandHandler.handleRequest(request, executionContext).block();

        long finalCount = mongoTemplate.count(new Query(), EntityChildrenOrdering.class);

        assertEquals(initialCount, finalCount, "Executing twice should not create duplicate records");

        Mockito.verify(orderedChildrenDocumentService, Mockito.times(2)).fetchFromDocument(documentId);
    }
}
