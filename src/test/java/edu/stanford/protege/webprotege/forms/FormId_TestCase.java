
package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FormId_TestCase {

    private FormId formId;

    private final String id = "12345678-1234-1234-1234-123456789abc";

    @BeforeEach
    public void setUp() {
        formId = FormId.get(id);
    }

    @Test
    public void shouldReturnSupplied_id() {
        assertThat(formId.getId(), is(this.id));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(formId, is(formId));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(formId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(formId, is(FormId.get(id)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_id() {
        assertThat(formId, is(not(FormId.get("1c11bb8a-d00d-4d02-ab5c-84498c0f513c"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(formId.hashCode(), is(FormId.get(id).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(formId.toString(), startsWith("FormId"));
    }

    @Test
    void shouldSerialize() throws IOException {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        var tester = new JacksonTester<>(FormId_TestCase.class,
                                     ResolvableType.forClass(FormId.class), objectMapper);
        var formId = FormId.generate();
        var written = tester.write(formId);
        assertThat(written.getJson(), equalTo("\"" + formId.getId() + "\""));
    }
    @Test
    void shouldDeserialize() throws IOException {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        var tester = new JacksonTester<>(FormId_TestCase.class,
                                     ResolvableType.forClass(FormId.class), objectMapper);
        var read = tester.read(new StringReader("\"12345678-1234-1234-1234-123456789abc\""));
        assertThat(read.getObject(), equalTo(FormId.valueOf("12345678-1234-1234-1234-123456789abc")));
    }
}
