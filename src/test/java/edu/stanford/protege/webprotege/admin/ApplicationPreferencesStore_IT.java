package edu.stanford.protege.webprotege.admin;

import com.mongodb.client.MongoClients;
import edu.stanford.protege.webprotege.app.ApplicationPreferences;
import edu.stanford.protege.webprotege.app.ApplicationPreferencesStore;
import edu.stanford.protege.webprotege.app.ApplicationLocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
@SpringBootTest
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
    public void shouldSaveSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        var count = countDocuments();
        assertThat(count, is(1L));
    }

    private long countDocuments() {
        return mongoTemplate.getCollection("ApplicationPreferences").countDocuments();
    }

    @Test
    public void shouldSaveSingleSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        manager.setApplicationPreferences(applicationPreferences);
        assertThat(countDocuments(), is(1L));
    }

    @Test
    public void shouldGetSavedSettings() {
        manager.setApplicationPreferences(applicationPreferences);
        ApplicationPreferences settings = manager.getApplicationPreferences();
        assertThat(settings, is(applicationPreferences));
    }

}
