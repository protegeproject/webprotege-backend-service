package edu.stanford.protege.webprotege.forms.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.forms.*;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
class FormDescriptorTest {

    @Autowired
    private JacksonTester<FormDescriptor> tester;

    @Test
    void shouldSerialize() throws IOException {
        var descriptor = new FormDescriptor(
                FormId.generate(),
                LanguageMap.empty(),
                List.of(),
                Optional.empty()
        );
        var written = tester.write(descriptor);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathMapValue("label");
        assertThat(written).hasJsonPathArrayValue("fields");
        assertThat(written).hasJsonPath("subjectFactory");
    }
}