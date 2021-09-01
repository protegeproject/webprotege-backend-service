package edu.stanford.protege.webprotege.user;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.auth.Salt;
import edu.stanford.protege.webprotege.auth.SaltedPasswordDigest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
@SpringBootTest
public class UserRecordRepository_TestCase {

    private static final String EMAIL_ADDRESS = "jane.doe@somewhere.com";

    @Autowired
    private UserRecordRepository recordRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private UserRecord userRecord;

    private UserId userId;

    @Before
    public void setUp() throws Exception {
        userId = UserId.valueOf("JaneDoe");
        userRecord = new UserRecord(
                userId,
                "Jane Doe" ,
                EMAIL_ADDRESS,
                "" ,
                new Salt("somebytes".getBytes()),
                new SaltedPasswordDigest("someotherbytes".getBytes())
        );
    }

    @After
    public void tearDown() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void shouldSaveUserRecord() {
        recordRepository.save(userRecord);
    }

    @Test
    public void shouldFindUserRecord() {
        recordRepository.save(userRecord);
        Optional<UserRecord> record = recordRepository.findOne(userId);
        assertThat(record.isPresent(), is(true));
        assertThat(record.get(), is(userRecord));
    }

    @Test
    public void shouldReplaceUserRecord() {
        recordRepository.save(userRecord);
        recordRepository.save(userRecord);
    }

    @Test
    public void shouldDeleteUserRecord() {
        recordRepository.save(userRecord);
        assertThat(recordRepository.findOne(userId).isPresent(), is(true));
        recordRepository.delete(userId);
        assertThat(recordRepository.findOne(userId).isPresent(), is(false));
    }

    @Test
    public void shouldFindByContaining() {
        recordRepository.save(userRecord);
        List<UserId> users = recordRepository.findByUserIdContainingIgnoreCase("doe", 1);
        assertThat(users, hasItem(userId));
    }

    @Test
    public void shouldFindByEmailAddress() {
        recordRepository.save(userRecord);
        Optional<UserRecord> record = recordRepository.findOneByEmailAddress(EMAIL_ADDRESS);
        assertThat(record.isPresent(), is(true));
    }
}
