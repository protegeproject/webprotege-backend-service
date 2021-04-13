package edu.stanford.bmir.protege.web.shared.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class EntityNode_Serialization_TestCase {

    @Test
    public void shouldSerializeNode() throws IOException {
        var entityNode = EntityNode.get(mockOWLClass(),
                       "The browser text",
                       ImmutableMap.of(DictionaryLanguage.localName(), "Hello"),
                       true,
                       ImmutableSet.of(Watch.create(mockUserId(), mockOWLClass(), WatchType.ENTITY)),
                       33,
                       ImmutableSet.of(Tag.get(TagId.createTagId(),
                                               mockProjectId(),
                                               "The Tag Label",
                                               "The tag description",
                                               Color.getWhite(),
                                               Color.getWhite(), ImmutableList.of())));
        JsonSerializationTestUtil.testSerialization(entityNode, EntityNode.class);
    }
}
