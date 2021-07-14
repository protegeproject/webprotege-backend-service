package edu.stanford.protege.webprotege.tag;

import com.google.common.collect.ImmutableList;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import edu.stanford.protege.webprotege.color.Color;
import edu.stanford.protege.webprotege.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.protege.webprotege.match.criteria.RootCriteria;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TagRepositoryImpl_IT {

    private static final String THE_TAG_LABEL = "The tag label";

    private static final String THE_TAG_DESCRIPTION = "The tag description";

    private static final Color COLOR = Color.getHex("#ffffff");

    private static final Color BG_COLOR = Color.getHex("#f0f0f0");

    public static final String COLLECTION_NAME = "Tags";

    @Autowired
    private TagRepositoryImpl repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Tag tag, tag2;

    private TagId tagId, tagId2;

    private ProjectId projectId, projectId2;

    private ImmutableList<RootCriteria> criteria;

    @Before
    public void setUp() throws Exception {
        tagId = TagId.getId("12345678-1234-1234-1234-123456789abc");
        tagId2 = TagId.getId("12345678-1234-1234-1234-123456789def");
        projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        projectId2 = ProjectId.get("12345678-1234-1234-1234-123456789def");

        repository.ensureIndexes();

        RootCriteria rootCriteria = EntityIsDeprecatedCriteria.get();
        criteria = ImmutableList.of(rootCriteria);
        tag = Tag.get(tagId,
                      projectId,
                      THE_TAG_LABEL,
                      THE_TAG_DESCRIPTION,
                      COLOR,
                      BG_COLOR,
                      criteria);
        tag2 = Tag.get(tagId2,
                       projectId2,
                       THE_TAG_LABEL,
                       THE_TAG_DESCRIPTION,
                       COLOR,
                       BG_COLOR,
                       criteria);


        repository.saveTag(tag);

    }

    @After
    public void tearDown() {
        mongoTemplate.getCollection(COLLECTION_NAME).drop();
    }

    /**
     * A util method that gets the tags collection size by querying the database using the
     * low level Java driver.
     */
    private long getTagsCollectionSize() {
        return mongoTemplate.getCollection(COLLECTION_NAME).countDocuments();
    }

    @Test
    public void shouldSaveTag() {
        long count = getTagsCollectionSize();
        assertThat(count, is(1L));
    }

    @Test
    public void shouldSaveTags() {
        repository.saveTags(Arrays.asList(tag, tag2));
        assertThat(getTagsCollectionSize(), is(2L));
    }

    @Test
    public void shouldFindTagByTagId() {
        Optional<Tag> theFoundTag = repository.findTagByTagId(tagId);
        assertThat(theFoundTag, is(Optional.of(tag)));
    }

    @Test
    public void shouldFindTagsByProjectId() {
        List<Tag> theTags = repository.findTags(projectId);
        assertThat(theTags, hasItems(tag));
    }

    @Test
    public void shouldNotDuplicateTags() {
        repository.saveTag(tag);
        assertThat(getTagsCollectionSize(), is(1L));
    }

    @Test
    public void shouldUpdateTag() {
        Tag updatedTag = Tag.get(tagId, projectId, "An updated label", THE_TAG_DESCRIPTION, COLOR, BG_COLOR, criteria);
        repository.saveTag(updatedTag);
        assertThat(getTagsCollectionSize(), is(1L));
        Optional<Tag> foundTag = repository.findTagByTagId(tagId);
        assertThat(foundTag, is(Optional.of(updatedTag)));
    }

    @Test
    public void shouldNotSaveTagWithDuplicateLabel() {
        try {
            TagId otherTagId = TagId.getId("1234abcd-abcd-abcd-abcd-123456789abc");
            Tag otherTag = Tag.get(otherTagId, projectId, THE_TAG_LABEL, THE_TAG_DESCRIPTION, COLOR, BG_COLOR, criteria);
            repository.saveTag(otherTag);
            fail("Inserted multiple documents");
        } catch (MongoWriteException e) {
            WriteError error = e.getError();
            assertThat(error.getCategory(), is(ErrorCategory.DUPLICATE_KEY));
        }

    }
}
