package edu.stanford.protege.webprotege.perspective;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.jackson.ObjectMapperProvider;
import edu.stanford.protege.webprotege.persistence.MongoTestUtils;
import edu.stanford.protege.webprotege.lang.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
@SpringBootTest
public class PerspectiveDescriptorRepository_TestCase {

    @Autowired
    private PerspectiveDescriptorRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        repository.ensureIndexes();
    }

    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(PerspectiveDescriptorRepositoryImpl.PERSPECTIVE_DESCRIPTORS);
    }

    @Test
    public void shouldSave() {
        var record = createTestRecord();
        repository.saveDescriptors(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    @Test
    public void shouldRetrieveSaved() {
        var record = createTestRecord();
        repository.saveDescriptors(record);
        var saved = repository.findDescriptors(record.getProjectId(), record.getUserId());
        assertThat(saved, is(Optional.of(record)));
    }

    @Test
    public void shouldNotSaveDuplicates() {
        var record = createTestRecord();
        repository.saveDescriptors(record);
        repository.saveDescriptors(record);
        assertThat(getCollection().countDocuments(), Matchers.is(1L));
    }

    @Test
    public void shouldGetLessSpecficDescriptors() {
        var userId = UserId.valueOf("TheUserName");
        var projectId = ProjectId.getNil();

        var userProjectRecord = PerspectiveDescriptorsRecord.get(projectId,
                                                                 userId,
                                                                 createPerspectivesList(PerspectiveId.generate()));
        repository.saveDescriptors(userProjectRecord);

        var projectPerspectiveId = PerspectiveId.generate();
        var projectRecord = PerspectiveDescriptorsRecord.get(projectId, createPerspectivesList(projectPerspectiveId));
        repository.saveDescriptors(projectRecord);

        var systemPerspectiveId = PerspectiveId.generate();
        var systemRecord = PerspectiveDescriptorsRecord.get(createPerspectivesList(systemPerspectiveId));
        repository.saveDescriptors(systemRecord);

        var result = repository.findProjectAndSystemDescriptors(projectId)
                               .map(PerspectiveDescriptorsRecord::getPerspectives)
                               .flatMap(Collection::stream)
                               .map(PerspectiveDescriptor::getPerspectiveId)
                               .collect(toSet());
        assertThat(result, hasItems(projectPerspectiveId, systemPerspectiveId));

    }

    private static ImmutableList<PerspectiveDescriptor> createPerspectivesList(PerspectiveId perspectiveId) {
        return ImmutableList.of(PerspectiveDescriptor.get(perspectiveId, LanguageMap.of("en", "Hello"), true));
    }

    private static PerspectiveDescriptorsRecord createTestRecord() {
        return PerspectiveDescriptorsRecord.get(ProjectId.getNil(),
                                                UserId.valueOf("Matthew"),
                                                createPerspectivesList(PerspectiveId.generate()));
    }

    @AfterEach
    public void tearDown() throws Exception {
        getCollection().drop();
    }
}
