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
class FormFieldDataTest {

    private JacksonTester<FormFieldData> tester;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldSerializeFormFieldDataWithCorrectFields() throws IOException {
        var data = FormFieldData.get(FormFieldDescriptor.get(FormRegionId.generate(),
                                                             null,
                                                             LanguageMap.empty(),
                                                             null,
                                                             null,
                                                             TextControlDescriptor.getDefault(),
                                                             Repeatability.NON_REPEATABLE,
                                                             Optionality.OPTIONAL,
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
                {"field":{"id":"5831c0f4-482a-4d72-9a05-bef6180dbc95","owlBinding":null,"label":{},"fieldRun":"START","control":{"@type":"TEXT","placeholder":{},"stringType":"SIMPLE_STRING","specificLangTag":"","lineMode":"SINGLE_LINE","pattern":"","patternViolationErrorMessage":{}},"repeatability":"NON_REPEATABLE","optionality":"OPTIONAL","readOnly":true,"help":{},"deprecationStrategy":"DELETE_VALUES","initialExpansionState":"COLLAPSED"},"data":{"pageElements":[],"pageSize":0,"totalElements":0,"pageNumber":1,"pageCount":1}}
                """;
        var read = tester.read(new StringReader(json));
        assertThat(read).isNotNull();
    }
}