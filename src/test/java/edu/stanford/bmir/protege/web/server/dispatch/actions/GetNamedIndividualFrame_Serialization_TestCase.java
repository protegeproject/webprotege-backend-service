package edu.stanford.bmir.protege.web.server.dispatch.actions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.dispatch.Action;
import edu.stanford.bmir.protege.web.server.dispatch.Result;
import edu.stanford.bmir.protege.web.server.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.server.frame.GetNamedIndividualFrameAction;
import edu.stanford.bmir.protege.web.server.frame.GetNamedIndividualFrameResult;
import edu.stanford.bmir.protege.web.server.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.bmir.protege.web.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetNamedIndividualFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetNamedIndividualFrameAction.create(mockProjectId(),
                                                          mockOWLNamedIndividual());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetNamedIndividualFrameResult.create(NamedIndividualFrame.get(
                OWLNamedIndividualData.get(mockOWLNamedIndividual(),
                                           ImmutableMap.of(), false), ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of()
        ));
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}