package edu.stanford.protege.webprotege.project;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.PulsarTestExtension;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.lang.DisplayNameSettings;
import edu.stanford.protege.webprotege.projectsettings.EntityDeprecationSettings;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.UserId;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 */
@SpringBootTest
@Import({WebprotegeBackendMonolithApplication.class})
@ExtendWith(PulsarTestExtension.class)
public class ProjectDetailsRepository_IT {

    public static final String COLLECTION_NAME = "ProjectDetails";

    public static final long CREATED_AT = 33L;

    public static final long MODIFIED_AT = 44L;

    public static final boolean IN_TRASH = true;

    @Autowired
    private ProjectDetailsRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ProjectId projectId = getProjectId();

    private ProjectId otherProjectId = getProjectId();
    

    private static ProjectId getProjectId() {
        return ProjectId.valueOf(UUID.randomUUID().toString());
    }

    private UserId owner = UserId.valueOf("The Owner");

    private UserId createdBy = UserId.valueOf("The Creator");

    private UserId lastModifiedBy = UserId.valueOf("The Editor");

    private UserId otherUser = UserId.valueOf("Other User");

    private ProjectDetails projectDetails;

    @BeforeEach
    public void setUp() {
        projectDetails = ProjectDetails.get(projectId,
                                            "The Display Name",
                                            "The Description",
                                            owner,
                                            IN_TRASH,
                                            DictionaryLanguage.rdfsLabel("en"),
                                            DisplayNameSettings.get(ImmutableList.of(DictionaryLanguage.rdfsLabel("en-GB"),
                                                                                     DictionaryLanguage.rdfsLabel("en"),
                                                                                     DictionaryLanguage.rdfsLabel("")),
                                                                    ImmutableList.of(DictionaryLanguage.rdfsLabel("de"))),
                                            CREATED_AT,
                                            createdBy,
                                            MODIFIED_AT,
                                            lastModifiedBy,
                                            EntityDeprecationSettings.empty());

        // Insert project details
        repository.save(projectDetails);
    }

    @AfterEach
    public void cleanUp() {
        getCollection().drop();
    }

    @Test
    public void shouldSaveProjectDetails() {
        assertThat(getCollection().countDocuments(), is(1L));
    }

    @Test
    public void shouldUpdateProjectDetails() {
        repository.save(projectDetails);
        assertThat(getCollection().countDocuments(), is(1L));
    }

    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection(COLLECTION_NAME);
    }

    @Test
    public void shouldFindByProjectId() {
        Optional<ProjectDetails> details = repository.findOne(projectId);
        assertThat(details, is(Optional.of(projectDetails)));
    }

    @Test
    public void shouldNotContainProjectDetails() {
        assertThat(repository.containsProject(otherProjectId), is(false));
    }

    @Test
    public void shouldContainProjectDetails() {
        repository.save(projectDetails);
        assertThat(repository.containsProject(projectId), is(true));
    }

    @Test
    public void shouldContainProjectWithOwner() {
        assertThat(repository.containsProjectWithOwner(projectId, owner), is(true));
    }

    @Test
    public void shouldNotContainProjectWithOwner() {
        assertThat(repository.containsProjectWithOwner(projectId, otherUser), is(false));
    }

    @Test
    public void shouldDeleteProject() {
        repository.delete(projectId);
        assertThat(getCollection().countDocuments(), is(0L));
    }

    @Test
    public void shouldFindProjectByOwner() {
        assertThat(repository.findByOwner(owner), is(singletonList(projectDetails)));
    }

    @Test
    public void shouldSetInTrashTrue() {
        repository.setInTrash(projectId, true);
        assertThat(repository.findOne(projectId).map(d -> d.isInTrash()), is(Optional.of(true)));
    }

    @Test
    public void shouldSetInTrashFalse() {
        repository.setInTrash(projectId, false);
        assertThat(repository.findOne(projectId).map(d -> d.isInTrash()), is(Optional.of(false)));
    }

    @Test
    public void shouldSetModified() {
        long modifiedAt = 55L;
        repository.setModified(projectId, modifiedAt, otherUser);
        Optional<ProjectDetails> projectDetails = repository.findOne(projectId);
        assertThat(projectDetails.isPresent(), is(true));
        if (projectDetails.isPresent()) {
            assertThat(projectDetails.get().getLastModifiedAt(), is(modifiedAt));
            assertThat(projectDetails.get().getLastModifiedBy(), is(otherUser));
        }
    }


}
