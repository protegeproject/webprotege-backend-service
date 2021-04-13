package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLClass;
import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLObjectProperty;

public class PlainClassFrame_Serialization_TestCase {

    private PlainClassFrame plainClassFrame;

    @Before
    public void setUp() throws Exception {
        var subject = mockOWLClass();
        var parents = ImmutableSet.of(mockOWLClass(), mockOWLClass());
        var propertyValues = ImmutableSet.<PlainPropertyValue>of(
                PlainPropertyClassValue.get(mockOWLObjectProperty(), mockOWLClass())
        );
        plainClassFrame = PlainClassFrame.get(subject, parents, propertyValues);
    }

    @Test
    public void shouldSerializePlainClassFrame() throws IOException {
        JsonSerializationTestUtil.testSerialization(plainClassFrame, PlainEntityFrame.class);
    }
}
