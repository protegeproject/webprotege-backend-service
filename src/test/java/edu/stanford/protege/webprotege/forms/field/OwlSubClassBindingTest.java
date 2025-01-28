package edu.stanford.protege.webprotege.forms.field;

import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.EntityIsNotDeprecatedCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.criteria.WebprotegeCriteriaApiApplication;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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