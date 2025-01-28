package edu.stanford.protege.webprotege.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class BuiltInPerspective_TestCase {

    private BuiltInPerspectiveLoader loader;

    @BeforeEach
    void setUp() {
        loader = new BuiltInPerspectiveLoader(new ObjectMapper());
    }

    @Test
    public void shouldLoadBuiltInClassesPerspective() throws IOException {
        var perspective = loader.load("builtin-perspective-data/Classes.json");
        assertThat(perspective.getPerspectiveId().getId(), is("69df8fa8-4f84-499e-9341-28eb5085c40b"));
        assertThat(perspective.getLabel().get("en"), is("Classes"));
        assertThat(perspective.getLayout(), is(not(nullValue())));
    }


}
