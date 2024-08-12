package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.*;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.forms.field.TextControlDescriptor;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import uk.ac.manchester.cs.owl.owlapi.*;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
class GridRowDataDtoTest {


    @Autowired
    private JacksonTester<GridRowDataDto> tester;

    @Test
    void shouldSerialize() throws IOException {
        var data = GridRowDataDto.get(ImmutableList.of());
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathArrayValue("cells");
        assertThat(written).hasJsonPath("subject");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {"subjectX":null,"cells":[]}
                """;
        var read = tester.read(new StringReader(json));
    }
}