package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.user.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
public class ApiKeyManager_IT {

    private static final UserId USER_ID = UserId.getUserId("JaneDoe");

    private static final String PURPOSE = "Test key";

    @Autowired
    private ApiKeyManager keyManager;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ApiKey generatedKey;

    private long timestamp;

    @Before
    public void setUp() throws Exception {
        timestamp = System.currentTimeMillis();
        generatedKey = generateApiKey();

    }

    private ApiKey generateApiKey() {
        return keyManager.generateApiKeyForUser(USER_ID, PURPOSE);
    }

    @Test
    public void shouldGenerateApiKey() {
        assertThat(generatedKey, is(not(nullValue())));
    }

    @Test
    public void shouldRevokeApiKeys() {
        keyManager.revokeApiKeys(USER_ID);
        assertThat(keyManager.getApiKeysForUser(USER_ID), is(empty()));
    }

    @Test
    public void shouldFindUserByGeneratedKey() {
        Optional<UserId> userId = keyManager.getUserIdForApiKey(generatedKey);
        assertThat(userId, is(Optional.of(USER_ID)));
    }

    @Test
    public void shouldFindKeyInfo() {
        long currentTime = System.currentTimeMillis();
        List<ApiKeyInfo> keyInfo = keyManager.getApiKeysForUser(USER_ID);
        assertThat(keyInfo.size(), is(1));
        ApiKeyInfo info = keyInfo.get(0);
        assertThat(info.getCreatedAt(), is(lessThanOrEqualTo(currentTime)));
        assertThat(info.getCreatedAt(), is(greaterThanOrEqualTo(timestamp)));
        assertThat(info.getPurpose(), is(PURPOSE));
    }

    @After
    public void tearDown() {
    }
}
