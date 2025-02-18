package edu.stanford.protege.webprotege.mail;

import edu.stanford.protege.webprotege.issues.CommentId;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CommentMessageIdGenerator_TestCase {

    private static final String COMMENT_ID_STRING = "comment.id.string";

    private static final String PROJECT_ID_STRING = "project.id.string";

    private CommentMessageIdGenerator generator;

    @Mock
    private MessageIdGenerator messageIdGenerator;

    private ProjectId projectId = ProjectId.generate();

    @Mock
    private CommentId commentId;

    @BeforeEach
    public void setUp() {
        when(commentId.getId()).thenReturn(COMMENT_ID_STRING);
        generator = new CommentMessageIdGenerator(messageIdGenerator);
    }

    @Test
public void shouldThrowNullPointerExceptionIf_ProjectId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        generator.generateCommentMessageId(null, commentId);
     });
}

    @Test
public void shouldThrowNullPointerExceptionIf_CommentId_IsNull() {
    assertThrows(NullPointerException.class, () -> { 
        generator.generateCommentMessageId(projectId, null);
     });
}

    @Test
    public void shouldGenerateMessageIdForComment() {
        generator.generateCommentMessageId(projectId, commentId);
        verify(messageIdGenerator, times(1)).generateProjectMessageId(projectId, "comments", COMMENT_ID_STRING);
    }
}
