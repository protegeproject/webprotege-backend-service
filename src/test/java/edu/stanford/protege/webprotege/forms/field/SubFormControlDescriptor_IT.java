package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.forms.ExpansionState;
import edu.stanford.protege.webprotege.forms.FormDescriptor;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-09
 */
@SpringBootTest
@Import(WebProtegeJacksonApplication.class)
public class SubFormControlDescriptor_IT {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldSerializeAndDeserialize() throws IOException {
        var formDescriptor = new FormDescriptor(FormId.get("12345678-1234-1234-1234-123456789abc"),
                                                LanguageMap.of("en", "The sub form"),
                                                singletonList(
                                                        FormFieldDescriptor.get(
                                                                FormRegionId.generate(),
                                                                OwlPropertyBinding.get(new OWLObjectPropertyImpl(
                                                                                               OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                                                                                       null),
                                                                LanguageMap.of("en", "The Label"),
                                                                FieldRun.START,
                                                                FormFieldDeprecationStrategy.LEAVE_VALUES_INTACT,
                                                                new TextControlDescriptor(
                                                                        LanguageMap.empty(),
                                                                        StringType.SIMPLE_STRING,
                                                                        "en",
                                                                        LineMode.SINGLE_LINE,
                                                                        "Pattern",
                                                                        LanguageMap.empty()
                                                                ),
                                                                Repeatability.NON_REPEATABLE,
                                                                Optionality.REQUIRED,
                                                                true,
                                                                ExpansionState.COLLAPSED,
                                                                LanguageMap.empty()
                                                        )
                                                ), Optional.empty());
        SubFormControlDescriptor descriptor = new SubFormControlDescriptor(formDescriptor);
        var serialized = objectMapper.writeValueAsString(descriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(SubFormControlDescriptor.class)
                .readValue(serialized);
        assertThat(deserialized, is(descriptor));
    }
}
