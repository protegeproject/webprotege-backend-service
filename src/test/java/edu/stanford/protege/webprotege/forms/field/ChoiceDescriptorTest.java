package edu.stanford.protege.webprotege.forms.field;

import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.data.PrimitiveFormControlData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
class ChoiceDescriptorTest {

    @Autowired
    private JacksonTester<ChoiceDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(ChoiceDescriptor.choice(LanguageMap.empty(),
                                             PrimitiveFormControlData.get("TextValue")));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("label");
        assertThat(written).hasJsonPathValue("value");
    }
}