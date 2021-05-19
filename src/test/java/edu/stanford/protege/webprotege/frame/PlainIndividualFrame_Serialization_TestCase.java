package edu.stanford.protege.webprotege.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.*;

public class PlainIndividualFrame_Serialization_TestCase {


    private PlainNamedIndividualFrame plainIndividualFrame;

    @Before
    public void setUp() throws Exception {
        var subject = mockOWLNamedIndividual();
        var parents = ImmutableSet.of(mockOWLClass(), mockOWLClass());
        var propertyValues = ImmutableSet.<PlainPropertyValue>of(
                PlainPropertyClassValue.get(mockOWLObjectProperty(), mockOWLClass())
        );
        var sameIndividuals = ImmutableSet.of(mockOWLNamedIndividual(), mockOWLNamedIndividual());
        plainIndividualFrame = PlainNamedIndividualFrame.get(subject, parents, sameIndividuals, propertyValues);
    }

    @Test
    public void shouldSerializePlainClassFrame() throws IOException {
        JsonSerializationTestUtil.testSerialization(plainIndividualFrame, PlainEntityFrame.class);
    }
}
