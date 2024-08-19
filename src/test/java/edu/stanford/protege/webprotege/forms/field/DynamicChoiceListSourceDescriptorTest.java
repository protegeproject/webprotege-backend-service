package edu.stanford.protege.webprotege.forms.field;

import edu.stanford.protege.webprotege.criteria.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
class DynamicChoiceListSourceDescriptorTest {

    @Autowired
    private JacksonTester<ChoiceListSourceDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(DynamicChoiceListSourceDescriptor.get(CompositeRootCriteria.get(List.of(), MultiMatchType.ALL)));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "Dynamic");
        assertThat(written).hasJsonPathValue("criteria");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {"@type":"Dynamic","criteria":{"match":"CompositeCriteria","criteria":[],"matchType":"ALL"}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read).isInstanceOf(DynamicChoiceListSourceDescriptor.class);
    }
}