package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.LiteralFormControlData;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
public class MultiChoiceControlDescriptor_Serialization_TestCase {

    @Autowired
    private JacksonTester<MultiChoiceControlDescriptor> tester;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void shouldSerialize_AnnotationComponentCriteria() throws IOException {
        var descriptor = MultiChoiceControlDescriptor.get(FixedChoiceListSourceDescriptor.get(List.of()), ImmutableList.of());
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathMapValue("choicesSource");
        assertThat(written).hasJsonPathArrayValue("defaultChoice");
    }

}
