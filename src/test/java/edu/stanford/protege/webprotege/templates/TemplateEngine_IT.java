package edu.stanford.protege.webprotege.templates;

import edu.stanford.protege.webprotege.MongoTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(properties = {"webprotege.rabbitmq.commands-subscribe=false"})
@ExtendWith({MongoTestExtension.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TemplateEngine_IT {

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void shouldRenderATemplateThroughTheApplicationContext() {
        // TemplateEngine resolves its MustacheFactory through a lazy Provider, so a
        // duplicate factory bean does not fail context startup - it only surfaces on the
        // first real render.  This pins that the provider resolves to exactly one bean.
        var body = templateEngine.populateTemplate("Hello {{name}}", Map.of("name", "WebProtege"));
        assertThat(body, is("Hello WebProtege"));
    }
}
