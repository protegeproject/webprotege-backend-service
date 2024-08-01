package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FormDataByFormIdTest {

    private JacksonTester<FormDataByFormId> tester;

    @BeforeEach
    void setUp() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new GuavaModule());
        tester = new JacksonTester<>(FormDataByFormIdTest.class,
                                     ResolvableType.forClass(FormDataByFormId.class), objectMapper);
    }

    @Test
    void shouldSerialize() throws IOException {
        var written = tester.write(new FormDataByFormId(Collections.emptyMap()));
        assertThat(written).isEqualToJson("{}");
    }

    @Test
    void shouldDeserialize() throws IOException {
        var read = tester.read(new StringReader("""
                                                        {
                                                        }
                                                        """));
        read.assertThat().isNotNull();
    }
}