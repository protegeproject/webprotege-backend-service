package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
public class NamedIndividual_Serialization_TestCase {
    @Test
    public void shouldSerializeFrame() throws IOException {
        Frame frame = NamedIndividualFrame.get(MockingUtils.mockOWLNamedIndividualData(),
                                                        ImmutableSet.of(),
                                                        ImmutableSet.of(),
                                                        ImmutableSet.of());
        JsonSerializationTestUtil.testSerialization(frame, Frame.class);
    }
}
