package edu.stanford.protege.webprotege.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.*;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

public class PlainObjectPropertyFrame_Serialization_TestCase {

    private PlainObjectPropertyFrame plainObjectPropertyFrame;

    @Before
    public void setUp() throws Exception {
        var subject = mockOWLObjectProperty();
        var parents = ImmutableSet.of(mockOWLObjectProperty(), mockOWLObjectProperty());
        var propertyValues = ImmutableSet.of(
                PlainPropertyAnnotationValue.get(mockOWLAnnotationProperty(), Literal("Hello"))
        );
        var domains = ImmutableSet.of(mockOWLClass());
        var ranges = ImmutableSet.of(mockOWLClass());
        var inverses = ImmutableSet.of(mockOWLObjectProperty());
        var characteristics = ImmutableSet.of(ObjectPropertyCharacteristic.FUNCTIONAL, ObjectPropertyCharacteristic.INVERSE_FUNCTIONAL);
        plainObjectPropertyFrame = PlainObjectPropertyFrame.get(subject, propertyValues, characteristics, domains, ranges, inverses);
    }

    @Test
    public void shouldSerializePlainObjectPropertyFrame() throws IOException {
        JsonSerializationTestUtil.testSerialization(plainObjectPropertyFrame, PlainEntityFrame.class);
    }

}
