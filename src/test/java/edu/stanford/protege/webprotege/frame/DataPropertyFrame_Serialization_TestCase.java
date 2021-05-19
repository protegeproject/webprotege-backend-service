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
public class DataPropertyFrame_Serialization_TestCase {

    @Test
    public void shouldSerializeFrame() throws IOException {
        DataPropertyFrame frame = DataPropertyFrame.get(MockingUtils.mockOWLDataPropertyData(),
                                                        ImmutableSet.of(),
                                                        ImmutableSet.of(),
                                                        ImmutableSet.of(),
                                                        true);
        JsonSerializationTestUtil.testSerialization(frame, Frame.class);
    }
}
