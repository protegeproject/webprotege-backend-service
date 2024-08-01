package edu.stanford.protege.webprotege.forms.data;

import com.google.common.collect.*;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.FilterState;
import edu.stanford.protege.webprotege.forms.field.GridControlDescriptor;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-07-03
 */
@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
public class GridControlDataDtoTest {

    @Autowired
    private JacksonTester<FormControlDataDto> tester;


    @Test
    void shouldSerialize() throws IOException {
        var data = GridControlDataDto.get(GridControlDescriptor.get(ImmutableList.of(), null), Page.emptyPage(),
                                          ImmutableSet.of(),
                                          3,
                                          FilterState.FILTERED);
        var written = tester.write(data);
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathStringValue("['@type']", "GridControlDataDto");
        assertThat(written).hasJsonPathValue("control");
        assertThat(written).hasJsonPathValue("rows");
        assertThat(written).hasJsonPathValue("ordering");
        assertThat(written).hasJsonPathValue("depth");
        assertThat(written).hasJsonPathValue("filterState");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {"@type":"GridControlDataDto","depth":3,"control":{"@type":"GRID","columns":[],"subjectFactory":null},"rows":{"pageElements":[],"pageSize":0,"totalElements":0,"pageNumber":1,"pageCount":1},"ordering":[],"filterState":"FILTERED"}
                """;
        tester.read(new StringReader(json));
    }
}
