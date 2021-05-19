package edu.stanford.protege.webprotege.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.mockOWLClassData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class ClassFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeFrame() throws IOException {
        ClassFrame frame = ClassFrame.get(mockOWLClassData(),
                                          ImmutableSet.of(),
                                          ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(frame, Frame.class);
    }
}
