package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.mockOWLNamedIndividual;
import static edu.stanford.protege.webprotege.MockingUtils.mockOWLObjectProperty;

public class PropertyIndividual_Serialization_TestCase {

    private PlainPropertyIndividualValue propertyValue;

    @Before
    public void setUp() throws Exception {
        propertyValue = PlainPropertyIndividualValue.get(mockOWLObjectProperty(),
                                                    mockOWLNamedIndividual());
    }

    @Test
    public void shouldSerializeAndDeserializePropertyValue() throws IOException {
        JsonSerializationTestUtil.testSerialization(propertyValue, PlainPropertyValue.class);
    }
}
