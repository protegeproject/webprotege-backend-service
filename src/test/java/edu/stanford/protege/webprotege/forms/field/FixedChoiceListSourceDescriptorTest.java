package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
class FixedChoiceListSourceDescriptorTest {

    @Autowired
    private JacksonTester<ChoiceListSourceDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(FixedChoiceListSourceDescriptor.get(ImmutableList.of()));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "Fixed");
        assertThat(written).hasJsonPathValue("choices");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {"@type":"Fixed","choices":[{"label":{},"value":{"type":"http://www.w3.org/2001/XMLSchema#string","value":"A"}}]}
                """;

        var read = tester.read(new StringReader(json));
        assertThat(read).isInstanceOf(FixedChoiceListSourceDescriptor.class);
        var obj = (FixedChoiceListSourceDescriptor) read.getObject();
        assertThat(obj.getChoices()).hasSize(1);
    }
}