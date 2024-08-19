package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.LanguageMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
class TextControlDescriptorTest {

    @Autowired
    private JacksonTester<TextControlDescriptor> tester;

    @Test
    public void shouldSerialize() throws IOException {
        var descriptor = new TextControlDescriptor(LanguageMap.of("en", "The placeholder"),
                                                   StringType.SPECIFIC_LANG_STRING,
                                                   "en",
                                                   LineMode.SINGLE_LINE,
                                                   "Pattern",
                                                   LanguageMap.of("en", "An error message"));
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("placeholder");
        assertThat(written).hasJsonPathStringValue("pattern", "Pattern");
        assertThat(written).hasJsonPathStringValue("stringType", "SPECIFIC_LANG_STRING");
        assertThat(written).hasJsonPathStringValue("specificLangTag", "en");
        assertThat(written).hasJsonPathStringValue("lineMode", "SINGLE_LINE");
        assertThat(written).hasJsonPathValue("patternViolationErrorMessage");
    }

    @Test
    void shouldNotSerializeEmpties() throws IOException {
        var descriptor = new TextControlDescriptor(LanguageMap.empty(),
                                                   StringType.SPECIFIC_LANG_STRING,
                                                   "",
                                                   LineMode.SINGLE_LINE,
                                                   "",
                                                   LanguageMap.empty());
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).doesNotHaveJsonPath("placeholder");
        assertThat(written).doesNotHaveJsonPath("pattern");
        assertThat(written).doesNotHaveJsonPath("specificLangTag");
        assertThat(written).doesNotHaveJsonPath("patternViolationErrorMessage");
    }

    @Test
    void shouldDeserializeWithNonIncludedEmpties() throws IOException {
        var json = """
                {
                		"@type": "TEXT",
                		"stringType": "LANG_STRING",
                		"lineMode": "SINGLE_LINE"
                }
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read.getObject().getPattern()).isEmpty();
        assertThat(read.getObject().getSpecificLangTag()).isEmpty();
        assertThat(read.getObject().getPlaceholder().asMap()).isEmpty();
        assertThat(read.getObject().getStringType()).isEqualTo(StringType.LANG_STRING);
        assertThat(read.getObject().getLineMode()).isEqualTo(LineMode.SINGLE_LINE);
    }
}