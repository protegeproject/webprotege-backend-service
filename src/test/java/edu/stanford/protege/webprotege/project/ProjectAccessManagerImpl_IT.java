
package edu.stanford.protege.webprotege.project;

import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@Import({WebprotegeBackendMonolithApplication.class})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProjectAccessManagerImpl_IT {

    public static final long TIMESTAMP_A = 33L;

    public static final long TIMESTAMP_B = 44L;

    @Autowired
    private ProjectAccessManagerImpl manager;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final ProjectId projectId = ProjectIdFactory.getFreshProjectId();

    private final UserId userId = UserId.valueOf("Jane Doe");

    private final UserId otherUserId = UserId.valueOf("John Smith");
    
    @BeforeEach
    public void setUp() throws Exception {
        manager.ensureIndexes();
    }

    @AfterEach
    public void tearDown() throws Exception {
        getCollection().drop();
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
        return mongoTemplate.getDb()
                          .getCollection("ProjectAccess");
    }


}
