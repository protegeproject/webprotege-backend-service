package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.MockingUtils.mockOWLAnnotationProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

public class PropertyAnnotationValue_Serialization_TestCase {

    private PlainPropertyAnnotationValue propertyValue;

    @Before
    public void setUp() throws Exception {
        propertyValue = PlainPropertyAnnotationValue.get(mockOWLAnnotationProperty(),
                                                      Literal("Hello"));
    }

    @Test
    public void shouldSerializeAndDeserializePropertyValue() throws IOException {
        JsonSerializationTestUtil.testSerialization(propertyValue, PlainPropertyValue.class);
    }
}
