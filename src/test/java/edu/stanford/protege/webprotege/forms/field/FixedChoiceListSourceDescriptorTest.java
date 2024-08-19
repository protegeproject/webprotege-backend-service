package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.criteria.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
                {"@type":"Fixed","choices":[]}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read).isInstanceOf(FixedChoiceListSourceDescriptor.class);
    }
}