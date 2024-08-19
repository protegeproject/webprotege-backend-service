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
import uk.ac.manchester.cs.owl.owlapi.*;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
@Import({WebProtegeJacksonApplication.class, WebprotegeCriteriaApiApplication.class})
class OwlSubClassBindingTest {


    @Autowired
    private JacksonTester<OwlBinding> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(OwlSubClassBinding.get(
                null
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "SUBCLASS");
        assertThat(written).doesNotHaveJsonPath("criteria");
    }

    @Test
    void shouldSerializeWithCriteria() throws IOException {
        var written = tester.write(OwlSubClassBinding.get(
                CompositeRootCriteria.get(List.of(EntityIsNotDeprecatedCriteria.get()), MultiMatchType.ALL)
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "SUBCLASS");
        assertThat(written).hasJsonPathMapValue("criteria");
    }

    @Test
    void shouldDeserializeWithoutCriteria() throws IOException {
        var json = """
                {"@type":"SUBCLASS"}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isInstanceOf(OwlSubClassBinding.class);

    }

    @Test
    void shouldDeserializeWithCriteria() throws IOException {
        var json = """
                {"@type":"SUBCLASS","criteria":{"matchType" : "ALL", "criteria" : [{"match":"EntityIsNotDeprecated"}]}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject()).isInstanceOf(OwlSubClassBinding.class);
        assertThat(((OwlSubClassBinding) read.getObject()).getCriteriaInternal()).isInstanceOf(CompositeRootCriteria.class);
    }
}