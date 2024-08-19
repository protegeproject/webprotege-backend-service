package edu.stanford.protege.webprotege.forms.field;

import edu.stanford.protege.webprotege.common.LanguageMap;
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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
public class GridColumnDescriptor_Serialization_Test {

    @Autowired
    private JacksonTester<GridColumnDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var descriptor = GridColumnDescriptor.get(
                FormRegionId.get("12345678-1234-1234-1234-123456789abc"),
                Optionality.OPTIONAL,
                Repeatability.NON_REPEATABLE,
                OwlPropertyBinding.get(new OWLAnnotationPropertyImpl(IRI.create("http://example.org/p"))),
                LanguageMap.empty(),
                EntityNameControlDescriptor.getDefault()
        );
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("id", "12345678-1234-1234-1234-123456789abc");
        assertThat(written).hasJsonPathValue("optionality");
        assertThat(written).hasJsonPathValue("repeatability");
        assertThat(written).hasJsonPathValue("owlBinding");
        assertThat(written).hasJsonPathValue("label");
        assertThat(written).hasJsonPathValue("control");
    }
}
