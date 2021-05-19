package edu.stanford.protege.webprotege.jackson;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import java.io.IOException;

import static edu.stanford.protege.webprotege.MockingUtils.mockIRI;

public class IRI_Serialization_TestCase {

    @Test
    public void shouldRoundTrip_IRI() throws IOException {
        var iri = mockIRI();
        JsonSerializationTestUtil.testSerialization(iri, IRI.class);
    }

    @Test(timeout = 1000)
    public void shouldRoundTrip_IRI_Against_OWLAnntationValue() throws IOException {
        var iri = mockIRI();
        JsonSerializationTestUtil.testSerialization(iri, OWLAnnotationValue.class);
    }
}
