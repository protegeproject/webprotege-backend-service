package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.mockOWLDataProperty;
import static edu.stanford.protege.webprotege.MockingUtils.mockOWLDatatype;

public class PropertyDatatypeValue_Serialization_TestCase {


    private PlainPropertyDatatypeValue propertyValue;

    @Before
    public void setUp() throws Exception {
        propertyValue = PlainPropertyDatatypeValue.get(mockOWLDataProperty(),
                                                       mockOWLDatatype());
    }

    @Test
    public void shouldSerializeAndDeserializePropertyValue() throws IOException {
        JsonSerializationTestUtil.testSerialization(propertyValue, PlainPropertyValue.class);
    }
}
