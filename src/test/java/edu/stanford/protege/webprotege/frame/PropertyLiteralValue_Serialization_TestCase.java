package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.mockOWLDataProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

public class PropertyLiteralValue_Serialization_TestCase {

    private PlainPropertyLiteralValue propertyValue;

    @Before
    public void setUp() throws Exception {
        propertyValue = PlainPropertyLiteralValue.get(mockOWLDataProperty(),
                                                      Literal("Hello"));
    }

    @Test
    public void shouldSerializeAndDeserializePropertyValue() throws IOException {
        JsonSerializationTestUtil.testSerialization(propertyValue, PlainPropertyValue.class);
    }
}
