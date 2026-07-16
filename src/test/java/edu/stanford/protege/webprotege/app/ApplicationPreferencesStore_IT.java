package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.MongoTestExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplicationPreferencesStore_IT {

    public static final String COLLECTION_NAME = "ApplicationPreferences";

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    public void tearDown() {
        mongoTemplate.getCollection(COLLECTION_NAME).drop();
    }

    @Test
    public void shouldPersistCompleteDefaultsOnAReadMissSoALaterReadSucceeds() {
        var firstStore = new ApplicationPreferencesStore(mongoTemplate);
        var defaults = firstStore.getApplicationPreferences();

        // A fresh store has no in-memory cache, so this read must come from the stored
        // document - previously the miss above planted a bare {_id: "Preferences"}
        // skeleton and this read failed to instantiate the preferences.
        var freshStore = new ApplicationPreferencesStore(mongoTemplate);
        assertThat(freshStore.getApplicationPreferences(), is(defaults));
    }

    @Test
    public void shouldReadBackExplicitlySavedPreferences() {
        var savedPreferences = new ApplicationPreferences("The app name",
                                                          "notify@example.org",
                                                          new ApplicationLocation("https", "example.org", "", 443),
                                                          42L);
        var firstStore = new ApplicationPreferencesStore(mongoTemplate);
        firstStore.setApplicationPreferences(savedPreferences);

        assertThat(mongoTemplate.findOne(new Query(), ApplicationPreferences.class), is(notNullValue()));

        // Previously any read of a stored document failed with "Cannot set property id"
        // because the id field was final and outside the persistence constructor.
        var freshStore = new ApplicationPreferencesStore(mongoTemplate);
        assertThat(freshStore.getApplicationPreferences(), is(savedPreferences));
    }
}
