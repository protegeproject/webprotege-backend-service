package edu.stanford.protege.webprotege.dispatch.actions;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.ontology.GetOntologyAnnotationsAction;
import edu.stanford.protege.webprotege.ontology.GetOntologyAnnotationsResult;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;


import static edu.stanford.protege.webprotege.MockingUtils.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-07
 */
public class GetOntologyAnnotations_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = GetOntologyAnnotationsAction.create(mockProjectId(),
                                                         Optional.empty());
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = GetOntologyAnnotationsResult.create(mockOWLOntologyID(), ImmutableList.of());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}