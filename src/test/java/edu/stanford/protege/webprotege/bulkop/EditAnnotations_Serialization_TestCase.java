package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class EditAnnotations_Serialization_TestCase {


    @Test
    public void shouldSerializeAction() throws IOException {
        var action = EditAnnotationsAction.create(
                ProjectId.getNil(),
                ImmutableSet.<OWLEntity>of(),
                Operation.DELETE, Optional.empty(),
                Optional.empty(),
                false,
                Optional.empty(),
                NewAnnotationData.get(Optional.empty(),
                                      Optional.empty(),
                                      Optional.empty()),
                "Msg"
        );
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }

    @Test
    public void shouldSerializeResult() throws IOException {
        var result = EditAnnotationsResult.get(MockingUtils.mockEventList());
        JsonSerializationTestUtil.testSerialization(result, Result.class);
    }
}
