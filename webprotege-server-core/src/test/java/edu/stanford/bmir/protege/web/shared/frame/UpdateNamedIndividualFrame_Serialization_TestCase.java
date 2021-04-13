package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

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