package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.WebprotegeBackendMonolithApplication;
import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
@JsonTest
@Import({WebProtegeJacksonApplication.class})
public class FormFieldDescriptor_Serialization_IT {

    @Autowired
    private JacksonTester<FormFieldDescriptor> tester;


    @Test
    public void shouldSerializeElementWithoutOwlBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormRegionId.get(UUID.randomUUID().toString()),
                null,
                LanguageMap.empty(),
                FieldRun.START,
                FormFieldDeprecationStrategy.DELETE_VALUES,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, "en", LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var serialized = tester.write(formElementDescriptor);
        var deserialized = tester.parse(serialized.getJson());
        assertThat(formElementDescriptor, is(deserialized.getObject()));
    }

    @Test
    public void shouldSerializeElementWithOwlPropertyBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormRegionId.get(UUID.randomUUID().toString()),
                OwlPropertyBinding.get(new OWLObjectPropertyImpl(IRI.create("http://example.org/prop")), null),
                LanguageMap.empty(),
                FieldRun.START,
                FormFieldDeprecationStrategy.DELETE_VALUES,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, "en", LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var serialized = tester.write(formElementDescriptor);
        System.out.println(serialized);
        var deserialized = tester.parse(serialized.getJson());
        assertThat(deserialized.getObject(), is(formElementDescriptor));
    }

    @Test
    public void shouldSerializeElementWithOwlClassBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormRegionId.get(UUID.randomUUID().toString()),
                OwlClassBinding.get(),
                LanguageMap.empty(),
                FieldRun.START,
                FormFieldDeprecationStrategy.DELETE_VALUES,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, "en", LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var serialized = tester.write(formElementDescriptor);
        System.out.println(serialized);
        var deserialized = tester.parse(serialized.getJson());
        assertThat(deserialized.getObject(), is(formElementDescriptor));
    }

    @Test
    public void shouldParseWithNoOwlBinding() throws IOException {
        var serializedForm = """
                {"id":"12345678-1234-1234-1234-123456789abc","label":{},"elementRun":"START","control":{"type":"TEXT","placeholder":{},"stringType":"SIMPLE_STRING","lineMode":"SINGLE_LINE","patternViolationErrorMessage":{}},"repeatability":"NON_REPEATABLE","optionality":"REQUIRED","help":{}}""";
        var deserializedForm = tester.parse(serializedForm);
        assertThat(deserializedForm.getObject().getOwlBinding().isEmpty(), is(true));

    }
}
