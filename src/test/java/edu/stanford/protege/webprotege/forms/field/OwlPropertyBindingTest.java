package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.criteria.*;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
class OwlPropertyBindingTest {

    @Autowired
    private JacksonTester<OwlBinding> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(OwlPropertyBinding.get(
                new OWLAnnotationPropertyImpl(IRI.create("http://example.org/p"))
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "PROPERTY");
        assertThat(written).hasJsonPathMapValue("property");
        assertThat(written).doesNotHaveJsonPath("criteria");
    }

    @Test
    void shouldSerializeWithCriteria() throws IOException {
        var written = tester.write(OwlPropertyBinding.get(
                new OWLAnnotationPropertyImpl(IRI.create("http://example.org/p"))
        , CompositeRelationshipValueCriteria.get(MultiMatchType.ALL, ImmutableList.of())));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "PROPERTY");
        assertThat(written).hasJsonPathMapValue("property");
        assertThat(written).hasJsonPathMapValue("criteria");
    }
}