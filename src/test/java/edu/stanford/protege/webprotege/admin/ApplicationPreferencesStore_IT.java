package edu.stanford.protege.webprotege.admin;

import edu.stanford.protege.webprotege.PulsarTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.app.ApplicationPreferences;
import edu.stanford.protege.webprotege.app.ApplicationPreferencesStore;
import edu.stanford.protege.webprotege.app.ApplicationLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
@SpringBootTest(classes = WebprotegeBackendMonolithApplication.class)
@ExtendWith(PulsarTestExtension.class)
public class ApplicationPreferencesStore_IT {

    private final ApplicationPreferences applicationPreferences = new ApplicationPreferences(
            "TheApplicationName" ,
            "TheSystemNotificationEmailAddress" ,
            new ApplicationLocation(
                    "TheScheme" ,
                    "TheHost" ,
                    "ThePath" ,
                    33
            ),
            44L
    );

    @Autowired
    private ApplicationPreferencesStore manager;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldWorkWithEmptyCollection() {
        var preferences = manager.getApplicationPreferences();
        assertThat(preferences).isNotNull();
    }

    @Test
    public void shouldSaveSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        var count = countDocuments();
        assertThat(count).isEqualTo(1);
    }

    private long countDocuments() {
        return mongoTemplate.getCollection("ApplicationPreferences").countDocuments();
    }

    @Test
    public void shouldSaveSingleSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        manager.setApplicationPreferences(applicationPreferences);
        assertThat(countDocuments()).isEqualTo(1);
    }

    @Test
    public void shouldGetSavedSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        ApplicationPreferences settings = manager.getApplicationPreferences();
        assertThat(settings).isEqualTo(applicationPreferences);
    }

    @AfterEach
    void tearDown() {
//        mongoTemplate.getCollection("ApplicationPreferences").drop();
    }
}
