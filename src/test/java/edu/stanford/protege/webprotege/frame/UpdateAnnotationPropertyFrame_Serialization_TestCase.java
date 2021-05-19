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
public class UpdateAnnotationPropertyFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeAction() throws IOException {
        var action = UpdateAnnotationPropertyFrameAction.create(
                mockProjectId(),
                PlainAnnotationPropertyFrame.get(
                        mockOWLAnnotationProperty(),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        ImmutableSet.of()
                ),
                PlainAnnotationPropertyFrame.get(
                        mockOWLAnnotationProperty(),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        ImmutableSet.of()
                )
        );
        JsonSerializationTestUtil.testSerialization(action, Action.class);
    }
}