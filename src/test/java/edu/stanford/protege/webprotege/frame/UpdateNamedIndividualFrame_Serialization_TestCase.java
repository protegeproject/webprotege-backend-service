package edu.stanford.protege.webprotege.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class UpdateNamedIndividualFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = UpdateNamedIndividualFrameAction.create(mockProjectId(),
                                                             PlainNamedIndividualFrame.get(
                                                                     mockOWLNamedIndividual(), ImmutableSet.of(),
                                                                     ImmutableSet.of(),
                                                                     ImmutableSet.of()
                                                             ),
                                                             PlainNamedIndividualFrame.get(
                                                                     mockOWLNamedIndividual(), ImmutableSet.of(),
                                                                     ImmutableSet.of(),
                                                                     ImmutableSet.of()
                                                             ));
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }
}