package edu.stanford.protege.webprotege.access;

import edu.stanford.protege.webprotege.MongoTestExtension;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.forms.FormRegionCapability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@AutoConfigureJsonTesters
@ExtendWith(MongoTestExtension.class)
public class ObjectMapperExtraCapabilitiesCustomizerTest {

    @Autowired
    private JacksonTester<Capability> tester;

    @Test
    public void shouldCustomizeWithFormRegionCapability() throws IOException {
        var json = """
                {
                    "@type" : "FormRegionCapability",
                    "id" : "EditFormField",
                    "formRegionId" : "bedad580-c60c-41a8-a5b4-a295d3b0070d"
                }
                """;
        var parsed = tester.parse(json);
        assertThat(parsed.getObject()).isInstanceOf(FormRegionCapability.class);
        var formRegionCapability = (FormRegionCapability) parsed.getObject();
        assertThat(formRegionCapability.formRegionId().value()).isEqualTo("bedad580-c60c-41a8-a5b4-a295d3b0070d");
        assertThat(formRegionCapability.id()).isEqualTo("EditFormField");
    }
}
