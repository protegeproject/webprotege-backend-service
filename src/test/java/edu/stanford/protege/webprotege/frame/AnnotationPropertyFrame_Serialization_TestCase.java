package edu.stanford.protege.webprotege.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class AnnotationPropertyFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeFrame() throws IOException {
        Frame frame = AnnotationPropertyFrame.get(MockingUtils.mockOWLAnnotationPropertyData(),
                                                  ImmutableSet.of(),
                                                  ImmutableSet.of(),
                                                  ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(frame, Frame.class);
    }
}
