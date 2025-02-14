package edu.stanford.protege.webprotege.icd.mappers;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.icd.dtos.EntityComment;
import edu.stanford.protege.webprotege.issues.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntityCommentMapperTest {

    @Test
    void givenCommentWithCreatedAtOnly_whenMapFromComment_thenEntityCommentIsCreated() {
        Comment comment = mock(Comment.class);
        long createdAtMillis = 1234567890123L;
        UserId createdBy = UserId.valueOf("user1");
        String body = "This is a comment.";

        when(comment.getCreatedAt()).thenReturn(createdAtMillis);
        when(comment.getCreatedBy()).thenReturn(createdBy);
        when(comment.getBody()).thenReturn(body);
        when(comment.getUpdatedAt()).thenReturn(Optional.empty());

        EntityComment result = EntityCommentMapper.mapFromComment(comment);

        assertEquals(createdBy, result.userId());
        assertEquals(ZonedDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.of("UTC")), result.createdAt());
        assertNull(result.updatedAt());
        assertEquals(body, result.body());

        verify(comment).getCreatedAt();
        verify(comment).getCreatedBy();
        verify(comment).getBody();
        verify(comment).getUpdatedAt();
    }

    @Test
    void givenCommentWithCreatedAtAndUpdatedAt_whenMapFromComment_thenEntityCommentIsCreatedWithUpdatedAt() {
        Comment comment = mock(Comment.class);
        long createdAtMillis = 1234567890123L;
        long updatedAtMillis = 1234567890124L;
        UserId createdBy = UserId.valueOf("user2");
        String body = "This is another comment.";

        when(comment.getCreatedAt()).thenReturn(createdAtMillis);
        when(comment.getCreatedBy()).thenReturn(createdBy);
        when(comment.getBody()).thenReturn(body);
        when(comment.getUpdatedAt()).thenReturn(Optional.of(updatedAtMillis));

        EntityComment result = EntityCommentMapper.mapFromComment(comment);

        assertEquals(createdBy, result.userId());
        assertEquals(ZonedDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.of("UTC")), result.createdAt());
        assertEquals(ZonedDateTime.ofInstant(Instant.ofEpochMilli(updatedAtMillis), ZoneId.of("UTC")), result.updatedAt());
        assertEquals(body, result.body());

        verify(comment).getCreatedAt();
        verify(comment).getCreatedBy();
        verify(comment).getBody();
        verify(comment).getUpdatedAt();
    }

    @Test
    void givenCommentWithNoCreatedBy_whenMapFromComment_thenEntityCommentHasNullCreatedBy() {
        Comment comment = mock(Comment.class);
        long createdAtMillis = 1234567890123L;

        when(comment.getCreatedAt()).thenReturn(createdAtMillis);
        when(comment.getCreatedBy()).thenReturn(null);
        when(comment.getBody()).thenReturn("No creator comment.");
        when(comment.getUpdatedAt()).thenReturn(Optional.empty());

        EntityComment result = EntityCommentMapper.mapFromComment(comment);

        assertNull(result.userId());
        assertEquals(ZonedDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.of("UTC")), result.createdAt());
        assertNull(result.updatedAt());
        assertEquals("No creator comment.", result.body());

        verify(comment).getCreatedAt();
        verify(comment).getCreatedBy();
        verify(comment).getBody();
        verify(comment).getUpdatedAt();
    }

    @Test
    void givenCommentWithEmptyBody_whenMapFromComment_thenEntityCommentHasEmptyBody() {
        Comment comment = mock(Comment.class);
        long createdAtMillis = 1234567890123L;

        when(comment.getCreatedAt()).thenReturn(createdAtMillis);
        when(comment.getCreatedBy()).thenReturn(UserId.valueOf("user3"));
        when(comment.getBody()).thenReturn("");
        when(comment.getUpdatedAt()).thenReturn(Optional.empty());

        EntityComment result = EntityCommentMapper.mapFromComment(comment);

        assertEquals(UserId.valueOf("user3"), result.userId());
        assertEquals(ZonedDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.of("UTC")), result.createdAt());
        assertNull(result.updatedAt());
        assertEquals("", result.body());

        verify(comment).getCreatedAt();
        verify(comment).getCreatedBy();
        verify(comment).getBody();
        verify(comment).getUpdatedAt();
    }
}