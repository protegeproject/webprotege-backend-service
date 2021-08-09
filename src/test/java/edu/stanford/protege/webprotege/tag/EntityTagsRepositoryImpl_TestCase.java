package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2018
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EntityTagsRepositoryImpl_TestCase {

    private static final String UUID = "12345678-1234-1234-1234-123456789abc";

    @Autowired
    private EntityTagsRepositoryImpl repository;

    private OWLEntity entity;

    private TagId tagIdA, tagIdB;

    private ProjectId projectId;

    private EntityTags entityTags;

    @Before
    public void setUp() throws Exception {
        projectId = ProjectId.get(UUID);
        repository.ensureIndexes();
        entity = new OWLClassImpl(IRI.create("http://stuff.com/entities/A"));
        tagIdA = TagId.getId("12345678-1234-1234-1234-123456789abc");
        tagIdB = TagId.getId("12345678-5678-5678-5678-123456789abc");
        List<TagId> tags = Arrays.asList(tagIdA, tagIdB);
        entityTags = new EntityTags(projectId,
                                    entity,
                                    tags);
    }

    @Test
    public void shouldSaveEntityTags() {
        repository.save(entityTags);
        assertThat(repository.findByEntity(entity, projectId), is(Optional.of(entityTags)));
    }

    @Test
    public void shouldNotSaveDuplicateEntityTags() {
        repository.save(entityTags);
        repository.save(entityTags);
        Optional<EntityTags> retrievedTags = repository.findByEntity(entity, projectId);
        assertThat(retrievedTags, is(Optional.of(entityTags)));
    }

    @Test
    public void shouldFindByTag() {
        repository.save(entityTags);
        assertThat(repository.findByTagId(tagIdA, projectId), hasItem(entityTags));
    }

    @Test
    public void shouldAddTag() {
        repository.save(entityTags);
        TagId theTagId = TagId.getId("12345678-abcd-abcd-abcd-123456789abc");
        repository.addTag(entity, theTagId, projectId);
        assertThat(repository.findByTagId(theTagId, projectId).size(), is(1));
    }

    @Test
    public void shouldRemoveTag() {
        repository.save(entityTags);
        repository.removeTag(entity, tagIdA, projectId);
        assertThat(repository.findByTagId(tagIdA, projectId).size(), is(0));
        assertThat(repository.findByTagId(tagIdB, projectId).size(), is(1));
    }
}
