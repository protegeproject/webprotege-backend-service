
package edu.stanford.protege.webprotege.project;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;

import static edu.stanford.protege.webprotege.persistence.MongoTestUtils.getTestDbName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ProjectAccessManagerImpl_IT {

    public static final long TIMESTAMP_A = 33L;

    public static final long TIMESTAMP_B = 44L;

    private ProjectAccessManagerImpl manager;

    private final ProjectId projectId = ProjectIdFactory.getFreshProjectId();

    private final UserId userId = UserId.valueOf("Jane Doe");

    private final UserId otherUserId = UserId.valueOf("John Smith");

    private MongoClient mongoClient;

    @Before
    public void setUp() throws Exception {
        mongoClient = MongoTestUtils.createMongoClient();
        MongoTemplate database = new MongoTemplate(mongoClient, MongoTestUtils.getTestDbName());
        manager = new ProjectAccessManagerImpl(database);
        manager.ensureIndexes();
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.getDatabase(getTestDbName()).drop();
        mongoClient.close();
    }

    @Test
    public void shouldSaveItem() {
        manager.logProjectAccess(projectId, userId, TIMESTAMP_A);
        assertThat(getCollection().countDocuments(), is(1L));
    }

    @Test
    public void shouldNotSaveDuplicateProjectUserItems() {
        manager.logProjectAccess(projectId, userId, TIMESTAMP_A);
        manager.logProjectAccess(projectId, userId, TIMESTAMP_B);
        assertThat(getCollection().countDocuments(), is(1L));
    }

    @Test
    public void shouldSaveSameProjectDifferentUsers() {
        manager.logProjectAccess(projectId, userId, TIMESTAMP_A);
        manager.logProjectAccess(projectId, otherUserId, TIMESTAMP_B);
        assertThat(getCollection().countDocuments(), is(2L));
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(getTestDbName())
                          .getCollection("ProjectAccess");
    }


}
