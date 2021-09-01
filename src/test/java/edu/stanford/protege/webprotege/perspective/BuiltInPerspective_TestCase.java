package edu.stanford.protege.webprotege.perspective;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
@SpringBootTest
public class BuiltInPerspective_TestCase {

    @Autowired
    private BuiltInPerspectiveLoader loader;

    @Test
    public void shouldLoadBuiltInClassesPerspective() throws IOException {
        var perspective = loader.load("builtin-perspective-data/Classes.json");
        assertThat(perspective.getPerspectiveId().getId(), is("69df8fa8-4f84-499e-9341-28eb5085c40b"));
        assertThat(perspective.getLabel().get("en"), is("Classes"));
        assertThat(perspective.getLayout(), is(not(nullValue())));
    }


}
