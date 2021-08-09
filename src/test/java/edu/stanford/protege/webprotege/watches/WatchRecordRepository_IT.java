package edu.stanford.protege.webprotege.watches;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WatchRecordRepository_IT {

    public static final String WATCHES = "Watches";

    @Autowired
    private WatchRecordRepositoryImpl repository;

    private UserId userId = UserId.getUserId("The User");

    private OWLEntity entity = new OWLClassImpl(IRI.create("http://the.ontology/ClsA"));

    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());

    @Autowired
    private MongoTemplate database;

    @Before
    public void setUp() throws Exception {
        repository.ensureIndexes();
    }

    @Test
    public void shouldSaveWatch() {
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        assertThat(getDocumentCount(), is(1L));
    }

    private long getDocumentCount() {
        return database.getCollection(WATCHES).countDocuments();
    }

    @Test
    public void shouldNotDuplicateWatch() {
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        assertThat(getDocumentCount(), is(1L));
    }

    @Test
    public void shouldReplaceWatchWithDifferentType() {
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.ENTITY));
        repository.saveWatchRecord(new WatchRecord(projectId, userId, entity, WatchType.BRANCH));
        assertThat(getDocumentCount(), is(1L));
        List<WatchRecord> watches = repository.findWatchRecords(projectId, userId, singleton(entity));
        assertThat(watches.size(), is(1));
        assertThat(watches.iterator().next().getType(), is(WatchType.BRANCH));
    }

    @Test
    public void shouldFindWatchByEntity() {
        WatchRecord watchRecord = new WatchRecord(projectId, userId, entity, WatchType.ENTITY);
        repository.saveWatchRecord(watchRecord);
        assertThat(repository.findWatchRecords(projectId, singleton(entity)), hasItem(watchRecord));
    }

    @Test
    public void shouldFindWatchByUserIdAndEntity() {
        WatchRecord watchRecord = new WatchRecord(projectId, userId, entity, WatchType.ENTITY);
        repository.saveWatchRecord(watchRecord);
        assertThat(repository.findWatchRecords(projectId, userId, singleton(entity)), hasItem(watchRecord));
    }

    @Test
    public void shouldDeleteWatchRecord() {
        WatchRecord watchRecord = new WatchRecord(projectId, userId, entity, WatchType.ENTITY);
        repository.saveWatchRecord(watchRecord);
        assertThat(getDocumentCount(), is(1L));
        repository.deleteWatchRecord(watchRecord);
        assertThat(getDocumentCount(), is(0L));
    }

    @After
    public void tearDown() throws Exception {
        database.getCollection(WATCHES).drop();
    }
}
