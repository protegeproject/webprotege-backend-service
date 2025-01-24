package edu.stanford.protege.webprotege.issues;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class CommentRenderer_TestCase {

    private CommentRenderer renderer;

    @BeforeEach
    public void setUp() throws Exception {
        renderer = new CommentRenderer();
    }

    @Test
    public void shouldRenderMentionedUsers() {
        String rendering = renderer.renderComment("Ask @{John Smith}");
        assertThat(rendering, containsString("<span class=\"wp-comment__user-mention\">John Smith</span>"));
    }
}
