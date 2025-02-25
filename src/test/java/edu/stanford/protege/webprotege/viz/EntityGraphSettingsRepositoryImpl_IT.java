package edu.stanford.protege.webprotege.viz;


import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EntityGraphSettingsRepositoryImpl_IT {

    private static final double RANK_SEPARATION = 2.0;

    @Autowired
    private EntityGraphSettingsRepositoryImpl repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ProjectId projectId;

    private UserId userId;

    @BeforeEach
    public void setUp() {
        userId = UserId.valueOf("JohnDoe");
        projectId = ProjectId.valueOf("12345678-1234-1234-1234-123456789abc");
    }

    @Test
    public void shouldSaveSettings() {
        var settings = ProjectUserEntityGraphSettings.getDefault(projectId, userId);
        repository.saveSettings(settings);
        var collection = mongoTemplate.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectUserEntityGraphSettings savedSettings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        assertThat(savedSettings, is(settings));
    }

    @Test
    public void shouldNotDuplicateSaveSettings() {
        var settings = ProjectUserEntityGraphSettings.getDefault(projectId, userId);
        repository.saveSettings(settings);
        repository.saveSettings(settings);
        var collection = mongoTemplate.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectUserEntityGraphSettings savedSettings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        assertThat(savedSettings, is(settings));
    }

    @Test
    public void shouldSaveProjectDefaultAndUserSettings() {
        var userSettings = ProjectUserEntityGraphSettings.getDefault(projectId, userId);
        repository.saveSettings(userSettings);

        var projectSettings = ProjectUserEntityGraphSettings.getDefault(projectId, null);
        repository.saveSettings(projectSettings);



        var collection = mongoTemplate.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(2L));

        var savedUserSettings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        assertThat(savedUserSettings, is(userSettings));

        var savedProjectSettings = repository.getSettingsForUserOrProjectDefault(projectId, null);
        assertThat(savedProjectSettings, is(projectSettings));
    }

    @Test
    public void shouldSaveSettingsWithoutUserId() {
        var settings = ProjectUserEntityGraphSettings.getDefault(projectId, null);
        repository.saveSettings(settings);
        var collection = mongoTemplate.getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName());
        assertThat(collection.countDocuments(), is(1L));
        ProjectUserEntityGraphSettings savedSettings = repository.getSettingsForUserOrProjectDefault(projectId, null);
        assertThat(savedSettings, is(settings));
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.getDb().getCollection(EntityGraphSettingsRepositoryImpl.getCollectionName()).drop();
    }
}
