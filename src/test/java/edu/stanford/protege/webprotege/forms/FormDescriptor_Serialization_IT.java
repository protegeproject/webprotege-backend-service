package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.LanguageMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
@JsonTest
public class FormDescriptor_Serialization_IT {

    private static final FormId FORM_ID = FormId.get("12345678-1234-1234-1234-123456789abc");

    private static final LanguageMap LABEL = LanguageMap.of("en", "The label");

    private OWLClass parent;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        parent = DataFactory.getOWLThing();
    }

    @Test
    public void shouldSerializeFormDescriptor() throws IOException {
        var subjectFactoryDescriptor = FormSubjectFactoryDescriptor.get(
                EntityType.CLASS,
                parent,
                Optional.of(IRI.create("http://example.org/target-ontology"))
        );
        var formDescriptor = new FormDescriptor(
                FORM_ID,
                LABEL,
                Collections.emptyList(),
                Optional.of(subjectFactoryDescriptor)
        );
        var serialized = objectMapper.writeValueAsString(formDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formDescriptor));
    }

    @Test
    public void shouldSerializeFormDescriptorWithoutTargetOntologyIri() throws IOException {
        var subjectFactoryDescriptor = FormSubjectFactoryDescriptor.get(
                EntityType.CLASS,
                parent,
                Optional.empty()
        );
        var formDescriptor = new FormDescriptor(
                FORM_ID,
                LABEL,
                Collections.emptyList(),
                Optional.of(subjectFactoryDescriptor)
        );
        var serialized = objectMapper.writeValueAsString(formDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formDescriptor));
    }

}
