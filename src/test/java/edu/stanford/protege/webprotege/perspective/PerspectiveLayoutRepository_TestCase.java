package edu.stanford.protege.webprotege.perspective;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveLayoutRepository_TestCase {

    private PerspectiveLayoutRepository repository;

    private MongoTemplate mongoTemplate;

    private MongoClient mongoClient;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        mongoTemplate = new MongoTemplate(mongoClient, MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        repository = new PerspectiveLayoutRepositoryImpl(mongoTemplate, objectMapper);
        repository.ensureIndexes();
    }

    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(PerspectiveLayoutRepositoryImpl.PERSPECTIVE_LAYOUTS);
    }

//    @Test
//    public void shouldCreateIndexes() {
//        var collection = getCollection();
//        try (var cursor = collection.listIndexes().cursor()) {
//            var index = cursor.tryNext();
//            assertThat(index, not(nullValue()));
//        }
//    }

    @Test
    public void shouldSave() {
        var record = createTestRecord();
        repository.saveLayout(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    /**
     * @noinspection ConstantConditions
     */
    @Test
    public void shouldRetrieveSaved() {
        var record = createTestRecord();
        repository.saveLayout(record);
        var saved = repository.findLayout(record.getProjectId(), record.getUserId(), record.getPerspectiveId());
        assertThat(saved, equalTo(Optional.of(record)));
    }

    @Test
    public void shouldNotSaveDuplicates() {
        var record = createTestRecord();
        repository.saveLayout(record);
        repository.saveLayout(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    /** @noinspection ConstantConditions*/
    @Test
    public void shouldDropLayout() {
        var record = createTestRecord();
        repository.saveLayout(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
        repository.dropLayout(record.getProjectId(), record.getUserId(), record.getPerspectiveId());
        assertThat(getCollection().countDocuments(), Matchers.is(0L));
    }

    private static PerspectiveLayoutRecord createTestRecord() {
        return PerspectiveLayoutRecord.get(ProjectId.getNil(),
                                           UserId.getUserId("Matthew"),
                                           PerspectiveId.generate(),
                                           null);
    }


    @After
    public void tearDown() throws Exception {
        mongoTemplate.getDb().drop();
        mongoClient.close();
    }
}
