package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.*;
import org.semanticweb.owlapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
class FormSubjectFactoryDescriptorTest {

    @Autowired
    private JacksonTester<FormSubjectFactoryDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var descriptor = FormSubjectFactoryDescriptor.get(
                EntityType.NAMED_INDIVIDUAL,
                new OWLClassImpl(IRI.create("http://example.org/A")),
                Optional.empty()
        );
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("entityType");
        assertThat(written).hasJsonPathValue("parent");
        assertThat(written).hasJsonPath("targetOntologyIri");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {"entityType":"NamedIndividual","parent":{"@type":"Class","iri":"http://example.org/A"},"targetOntologyIri":null}
                """;
        var expected = FormSubjectFactoryDescriptor.get(
                EntityType.NAMED_INDIVIDUAL,
                new OWLClassImpl(IRI.create("http://example.org/A")),
                Optional.empty()
        );
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isEqualTo(expected);

    }
}