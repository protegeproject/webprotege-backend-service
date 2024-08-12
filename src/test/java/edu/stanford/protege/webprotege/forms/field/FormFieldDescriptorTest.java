package edu.stanford.protege.webprotege.forms.field;

import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.ExpansionState;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
class FormFieldDescriptorTest {

    @Autowired
    private JacksonTester<FormFieldDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var descriptor = FormFieldDescriptor.get(
                FormRegionId.generate(),
                OwlPropertyBinding.get(new OWLAnnotationPropertyImpl(IRI.create("http://example.org"))),
                LanguageMap.empty(),
                FieldRun.START,
                FormFieldDeprecationStrategy.DELETE_VALUES,
                EntityNameControlDescriptor.getDefault(),
                Repeatability.NON_REPEATABLE,
                Optionality.OPTIONAL,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("id");
        assertThat(written).hasJsonPathValue("owlBinding");
        assertThat(written).hasJsonPathValue("label");
        assertThat(written).hasJsonPathValue("fieldRun");
        assertThat(written).hasJsonPathValue("deprecationStrategy");
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("repeatability");
        assertThat(written).hasJsonPathValue("optionality");
        assertThat(written).hasJsonPathValue("readOnly");
        assertThat(written).hasJsonPathValue("initialExpansionState");
        assertThat(written).hasJsonPathValue("help");
    }
}