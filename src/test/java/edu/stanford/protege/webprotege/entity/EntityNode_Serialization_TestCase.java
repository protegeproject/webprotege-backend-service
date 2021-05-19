package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.color.Color;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.tag.Tag;
import edu.stanford.protege.webprotege.tag.TagId;
import edu.stanford.protege.webprotege.watches.Watch;
import edu.stanford.protege.webprotege.watches.WatchType;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.*;

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
