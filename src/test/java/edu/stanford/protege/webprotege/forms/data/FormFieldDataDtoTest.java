package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.forms.ExpansionState;
import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(WebProtegeJacksonApplication.class)
class FormFieldDataDtoTest {


    private JacksonTester<FormFieldDataDto> tester;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldSerializeFormFieldDataWithCorrectFields() throws IOException {
        var data = FormFieldDataDto.get(FormFieldDescriptorDto.get(
                FormRegionId.generate(),
                                                             null,
                                                             LanguageMap.empty(),
                                                             FieldRun.START,
                                                             TextControlDescriptorDto.get(TextControlDescriptor.getDefault()),
                                                                   Optionality.OPTIONAL,
                                                             Repeatability.NON_REPEATABLE,
                                                             FormFieldDeprecationStrategy.DELETE_VALUES,
                                                             true,
                                                             ExpansionState.COLLAPSED,
                                                             LanguageMap.empty()), Page.emptyPage());

        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathMapValue("field");
        assertThat(written).hasJsonPathMapValue("data");
    }

    @Test
    void shouldDeserializeFormFieldData() throws IOException {
        var json = """
                {"field":{"id":"4eeb6550-58e3-4103-a875-7c0002f2fc93","owlBinding":null,"label":{},"fieldRun":"START","control":{"@type":"TextControlDescriptorDto","control":{"@type":"TEXT","placeholder":{},"stringType":"SIMPLE_STRING","specificLangTag":"","lineMode":"SINGLE_LINE","pattern":"","patternViolationErrorMessage":{}}},"optionality":"OPTIONAL","repeatability":"NON_REPEATABLE","deprecationStrategy":"DELETE_VALUES","readOnly":true,"initialExpansionState":"COLLAPSED","help":{}},"data":{"pageElements":[],"pageSize":0,"totalElements":0,"pageNumber":1,"pageCount":1}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read).isNotNull();
    }
}