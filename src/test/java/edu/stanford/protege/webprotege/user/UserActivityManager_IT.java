package edu.stanford.protege.webprotege.user;

import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.project.RecentProjectRecord;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@SpringBootTest(classes = WebprotegeBackendMonolithApplication.class, properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class UserActivityManager_IT {

    public static final long LAST_LOGIN = 33L;

    public static final long NEW_LAST_LOGIN = 88L;

    public static final long LAST_LOGOUT = 44L;

    public static final long NEW_LAST_LOGOUT = 99L;

    public static final long RECENT_PROJECT_TIMESTAMP = 77L;

    private UserActivityManager repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private UserId userId = UserId.valueOf("John Smith" );

    private ProjectId projectId = ProjectId.valueOf(UUID.randomUUID().toString());

    private UserActivityRecord record = new UserActivityRecord(userId,
                                                           LAST_LOGIN,
                                                           LAST_LOGOUT,
                                                           singletonList(
                                                                   new RecentProjectRecord(projectId, 55L)));


    @BeforeEach
    public void setUp() throws Exception {
        repository = new UserActivityManager(mongoTemplate);
    }

    @AfterEach
    public void tearDown() throws Exception {
         mongoTemplate.getDb().drop();
    }

    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection("UserActivity");
    }

    @Test
    public void shouldSaveUserActivityRecord() {
        repository.save(record);
        assertThat(getCollection().countDocuments(), is(1L));
    }

    @Test
    public void shouldNotSaveDuplicateUserActivityRecord() {
        repository.save(record);
        repository.save(record);
        assertThat(getCollection().countDocuments(), is(1L));
    }

    @Test
    public void shouldFindUserActivityRecord() {
        repository.save(record);
        assertThat(repository.getUserActivityRecord(userId), is(Optional.of(record)));
    }

    @Test
    public void shouldSetLastLogin() {
        repository.save(record);
        long lastLogin = NEW_LAST_LOGIN;
        repository.setLastLogin(userId, lastLogin);
        Optional<UserActivityRecord> record = repository.getUserActivityRecord(userId);
        assertThat(record.get().getLastLogin(), is(lastLogin));
    }

    @Test
    public void shouldSetLastLogout() {
        repository.save(record);
        long lastLogOut = NEW_LAST_LOGOUT;
        repository.setLastLogout(userId, lastLogOut);
        Optional<UserActivityRecord> record = repository.getUserActivityRecord(userId);
        assertThat(record.get().getLastLogout(), is(lastLogOut));
    }

    @Test
    public void shouldAddRecentProject() {
        repository.save(record);
        long timestamp = RECENT_PROJECT_TIMESTAMP;
        repository.addRecentProject(userId, projectId, timestamp);
        Optional<UserActivityRecord> record = repository.getUserActivityRecord(userId);
        assertThat(record.get().getRecentProjects(), Matchers.contains(new RecentProjectRecord(projectId,
                                                                                               timestamp)));
    }


}
