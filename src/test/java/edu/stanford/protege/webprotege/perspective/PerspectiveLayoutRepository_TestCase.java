package edu.stanford.protege.webprotege.perspective;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
@SpringBootTest
public class PerspectiveLayoutRepository_TestCase {

    @Autowired
    private PerspectiveLayoutRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;


    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(PerspectiveLayoutRepositoryImpl.PERSPECTIVE_LAYOUTS);
    }

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
                                           UserId.valueOf("Matthew"),
                                           PerspectiveId.generate(),
                                           null);
    }


    @AfterEach
    public void tearDown() throws Exception {
        getCollection().drop();
    }
}
