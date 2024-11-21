package edu.stanford.protege.webprotege.icd.mappers;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.icd.dtos.EntityCommentThread;
import edu.stanford.protege.webprotege.issues.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntityCommentThreadMapperTest {

    @Test
    void givenDiscussionThreadWithMultipleComments_whenMapFromDiscussionThread_thenEntityCommentThreadIsCreated() {
        ProjectId projectId = ProjectId.generate();
        ThreadId threadId = ThreadId.create();

        Comment comment1 = mock(Comment.class);
        Comment comment2 = mock(Comment.class);

        when(comment1.getBody()).thenReturn("Comment 1");
        when(comment1.getCreatedBy()).thenReturn(UserId.valueOf("user1"));

        when(comment2.getBody()).thenReturn("Comment 2");
        when(comment2.getCreatedBy()).thenReturn(UserId.valueOf("user2"));

        EntityDiscussionThread discussionThread = new EntityDiscussionThread(
                threadId,
                projectId,
                DataFactory.getOWLClass("entity1"),
                Status.OPEN,
                ImmutableList.of(comment1, comment2)
        );

        EntityCommentThread result = EntityCommentThreadMapper.mapFromDiscussionThread(discussionThread);

        assertNotNull(result);
        assertEquals(projectId, result.projectId());
        assertEquals("entity1", result.entityIRI());
        assertEquals("OPEN", result.status());
        assertEquals(2, result.entityComments().size());

        assertEquals("Comment 1", result.entityComments().get(0).body());
        assertEquals("user1", result.entityComments().get(0).userId().id());
        assertEquals("Comment 2", result.entityComments().get(1).body());
        assertEquals("user2", result.entityComments().get(1).userId().id());
    }

    @Test
    void givenDiscussionThreadWithNoComments_whenMapFromDiscussionThread_thenEntityCommentThreadHasEmptyComments() {
        ProjectId projectId = ProjectId.generate();
        ThreadId threadId = ThreadId.create();

        EntityDiscussionThread discussionThread = new EntityDiscussionThread(
                threadId,
                projectId,
                DataFactory.getOWLClass("entity2"),
                Status.CLOSED,
                ImmutableList.of()
        );

        EntityCommentThread result = EntityCommentThreadMapper.mapFromDiscussionThread(discussionThread);

        assertNotNull(result);
        assertEquals(projectId, result.projectId());
        assertEquals("entity2", result.entityIRI());
        assertEquals("CLOSED", result.status());
        assertTrue(result.entityComments().isEmpty());
    }
}
