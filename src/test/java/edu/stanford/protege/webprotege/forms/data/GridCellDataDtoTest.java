package edu.stanford.protege.webprotege.forms.data;

import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.forms.FilterState;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.jackson.WebProtegeJacksonApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@AutoConfigureJsonTesters
@Import(WebProtegeJacksonApplication.class)
public class GridCellDataDtoTest {

    @Autowired
    JacksonTester<GridCellDataDto> tester;

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(GridCellDataDto.get(
                FormRegionId.generate(),
                Page.emptyPage(),
                FilterState.FILTERED
        ));
        System.out.println(written.getJson());
        assertThat(written).hasJsonPathValue("columnId");
        assertThat(written).hasJsonPathValue("values");
        assertThat(written).hasJsonPathValue("filterState");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var json = """
                {"columnId":"a60a9e62-367f-4441-8dda-6c1554447e0d","values":{"pageElements":[],"pageSize":0,"totalElements":0,"pageNumber":1,"pageCount":1},"filterState":"FILTERED"}
                """;
        var read = tester.read(new StringReader(json));
        var obj = read.getObject();
        assertThat(obj.getColumnId().value()).isEqualTo("a60a9e62-367f-4441-8dda-6c1554447e0d");
        assertThat(obj.getFilterState()).isEqualTo(FilterState.FILTERED);
    }
}