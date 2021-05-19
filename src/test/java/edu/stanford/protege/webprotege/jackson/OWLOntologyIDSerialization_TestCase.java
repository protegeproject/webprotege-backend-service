package edu.stanford.protege.webprotege.jackson;

import edu.stanford.protege.webprotege.match.JsonSerializationTestUtil;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-06
 */
public class OWLOntologyIDSerialization_TestCase {
    
    @Test
    public void shouldSerializeOntologyWithOntologyIri() throws IOException {
        JsonSerializationTestUtil.testSerialization(new OWLOntologyID(IRI.create("http://example.org/ont")), OWLOntologyID.class);
    }

    @Test
    public void shouldSerializeOntologyWithOntologyIriAndVersionIri() throws IOException {
        JsonSerializationTestUtil.testSerialization(new OWLOntologyID(IRI.create("http://example.org/ont"),
                                                                      IRI.create("http://example.org/ont/v1")),
                                                    OWLOntologyID.class);
    }
}
