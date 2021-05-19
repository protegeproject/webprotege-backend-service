package edu.stanford.protege.webprotege.dispatch.actions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLNamedIndividualData;
import edu.stanford.protege.webprotege.frame.GetNamedIndividualFrameAction;
import edu.stanford.protege.webprotege.frame.GetNamedIndividualFrameResult;
import edu.stanford.protege.webprotege.frame.NamedIndividualFrame;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;


import static edu.stanford.protege.webprotege.MockingUtils.*;

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