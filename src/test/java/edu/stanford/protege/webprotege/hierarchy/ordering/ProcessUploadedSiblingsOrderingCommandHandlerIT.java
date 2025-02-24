package edu.stanford.protege.webprotege.hierarchy.ordering;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;
import java.nio.file.*;
import java.util.*;

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

    @MockitoSpyBean
    private OrderedChildrenDocumentService orderedChildrenDocumentService;

    @MockitoSpyBean
    private MinioFileDownloader minioFileDownloader;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String testJsonContent;
    private final String documentId = "test-document-id";
    private final ProjectId projectId = new ProjectId(UUID.randomUUID().toString());

    @BeforeEach
    void setUp() throws IOException {
        mongoTemplate.dropCollection(ProjectOrderedChildren.class);

        String jsonFilePath = "src/test/resources/orderedSiblingsTest.json";
        testJsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

        Mockito.doAnswer(invocation -> new ByteArrayInputStream(testJsonContent.getBytes()))
                .when(minioFileDownloader).fetchDocument(anyString());
    }

    @Test
    public void GIVEN_uploadedDocument_WHEN_handleRequestCalled_THEN_batchesAreProcessedAndPersisted() {
        var request = new ProcessUploadedSiblingsOrderingAction(projectId, new DocumentId(documentId),false);
        var executionContext = new ExecutionContext();

        var response = commandHandler.handleRequest(request, executionContext).block();

        assertNotNull(response, "Response should not be null");

        List<ProjectOrderedChildren> persistedParents = mongoTemplate.findAll(ProjectOrderedChildren.class);
        assertFalse(persistedParents.isEmpty(), "Processed parents should be saved to MongoDB");

        for (ProjectOrderedChildren parent : persistedParents) {
            assertNotNull(parent.children(), "Children list should not be null");

            Query query = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parent.entityUri()));
            ProjectOrderedChildren storedParent = mongoTemplate.findOne(query, ProjectOrderedChildren.class);

            assertNotNull(storedParent, "Parent entity should be present in MongoDB");
            assertEquals(parent.children().size(), storedParent.children().size(), "Child count should match stored data");
        }

        Mockito.verify(orderedChildrenDocumentService, Mockito.times(1)).fetchFromDocument(documentId);
    }

    @Test
    public void GIVEN_uploadedDocument_WHEN_handleRequestCalledTwice_THEN_noDuplicatesAreCreated() {
        var request = new ProcessUploadedSiblingsOrderingAction(projectId, new DocumentId(documentId), false);
        var executionContext = new ExecutionContext();

        commandHandler.handleRequest(request, executionContext).block();

        List<ProjectOrderedChildren> initialParents = mongoTemplate.findAll(ProjectOrderedChildren.class);

        commandHandler.handleRequest(request, executionContext).block();

        List<ProjectOrderedChildren> finalParents = mongoTemplate.findAll(ProjectOrderedChildren.class);

        assertEquals(initialParents.size(), finalParents.size(), "Parent entity count should remain the same");

        for (ProjectOrderedChildren parent : finalParents) {
            Query query = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parent.entityUri()));
            ProjectOrderedChildren storedParent = mongoTemplate.findOne(query, ProjectOrderedChildren.class);

            assertNotNull(storedParent, "Parent entity should exist in MongoDB");
            assertEquals(parent.children().size(), storedParent.children().size(), "Child list should not have duplicates");
        }

        Mockito.verify(orderedChildrenDocumentService, Mockito.times(2)).fetchFromDocument(documentId);
    }

    @Test
    public void GIVEN_uploadedDocument_WHEN_overrideExistingFalse_THEN_onlyNewEntriesAreAdded() {
        String parentUri = "http://id.who.int/icd/entity/360081115";
        List<String> existingChildren = List.of("http://id.who.int/icd/entity/oldChild");
        ProjectOrderedChildren existingEntry = new ProjectOrderedChildren(parentUri, projectId, existingChildren, null);

        mongoTemplate.insert(existingEntry);

        var request = new ProcessUploadedSiblingsOrderingAction(projectId, new DocumentId(documentId), false);
        var executionContext = new ExecutionContext();
        commandHandler.handleRequest(request, executionContext).block();

        Query query = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentUri));
        ProjectOrderedChildren storedEntry = mongoTemplate.findOne(query, ProjectOrderedChildren.class);

        assertNotNull(storedEntry, "Parent should still exist after processing");
        assertEquals(existingChildren, storedEntry.children(), "Existing children should remain");

        Mockito.verify(orderedChildrenDocumentService, Mockito.times(1)).fetchFromDocument(documentId);
    }

    @Test
    public void GIVEN_uploadedDocument_WHEN_overrideExistingTrue_THEN_existingDataIsOverwritten() {
        String parentUri = "http://id.who.int/icd/entity/360081115";
        List<String> existingChildren = List.of("http://id.who.int/icd/entity/oldChild");
        ProjectOrderedChildren existingEntry = new ProjectOrderedChildren(parentUri, projectId, existingChildren, null);
        mongoTemplate.insert(existingEntry);

        var request = new ProcessUploadedSiblingsOrderingAction(projectId, new DocumentId(documentId), true);
        var executionContext = new ExecutionContext();
        commandHandler.handleRequest(request, executionContext).block();

        Query query = new Query(Criteria.where(ProjectOrderedChildren.ENTITY_URI).is(parentUri));
        ProjectOrderedChildren storedEntry = mongoTemplate.findOne(query, ProjectOrderedChildren.class);

        assertNotNull(storedEntry, "Parent should still exist after processing");
        assertNotEquals(existingChildren, storedEntry.children(), "Children should be completely replaced");
        assertEquals(15, storedEntry.children().size(), "Children should match the test JSON file");
        assertFalse(storedEntry.children().containsAll(existingChildren));
        Mockito.verify(orderedChildrenDocumentService, Mockito.times(1)).fetchFromDocument(documentId);
    }
}
