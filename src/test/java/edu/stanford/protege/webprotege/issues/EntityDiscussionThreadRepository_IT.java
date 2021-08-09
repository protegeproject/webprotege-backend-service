package edu.stanford.protege.webprotege.issues;

import com.google.common.collect.ImmutableList;
import com.mongodb.client.MongoCollection;
import edu.stanford.protege.webprotege.MockingUtils;
import edu.stanford.protege.webprotege.project.ProjectIdFactory;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 *
 * An integration test for the repo that stores entity discussion thread.  This test requires
 * a running version of MongoDB.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class EntityDiscussionThreadRepository_IT {

    private final ProjectId projectId = ProjectIdFactory.getFreshProjectId();

    private final OWLClass entity = MockingUtils.mockOWLClass();

    @Autowired
    private MongoTemplate mongoTemplate;

    private EntityDiscussionThread thread;

    private EntityDiscussionThreadRepository repository;

    private Comment comment;

    @Before
    public void setUp() throws Exception {
        repository = new EntityDiscussionThreadRepository(mongoTemplate);
        comment = new Comment(
                CommentId.create(),
                UserId.getUserId("John"),
                System.currentTimeMillis(),
                Optional.of(33L),
                "The body", "The rendered body");
        thread = new EntityDiscussionThread(ThreadId.create(),
                                            projectId,
                                            entity,
                                            Status.OPEN,
                                            ImmutableList.of(comment)
        );
        repository.saveThread(thread);
    }

    @After
    public void tearDown() throws Exception {
        mongoTemplate.getDb().drop();
    }


    @Test
    public void shouldSaveItem() {
        long count = getCollection().countDocuments(new Document("_id", thread.getId().getId()));
        assertThat(count, is(1L));
    }

    @Test
    public void shouldFindThread() {
        Optional<EntityDiscussionThread> foundThread = repository.getThread(thread.getId());
        assertThat(foundThread, is(Optional.of(thread)));
    }

    @Test
    public void shouldFindThreadByProjectIdAndEntity() {
        List<EntityDiscussionThread> threads = repository.findThreads(projectId, entity);
        assertThat(threads, hasItem(thread));
    }

    @Test
    public void shouldNotSaveItemTwice() {
        repository.saveThread(thread);
        long count = getCollection().countDocuments(new Document("_id", thread.getId().getId()));
        assertThat(count, is(1L));
    }

    @Test
    public void shouldAddComment() {
        repository.saveThread(thread);
        long createdAt = System.currentTimeMillis();
        Comment theComment = new Comment(
                CommentId.create(),
                UserId.getUserId("Matthew"),
                                      createdAt,
                                      Optional.empty(),
                                      "The body", "The rendered body");
        repository.addCommentToThread(thread.getId(),
                                      theComment);
        Optional<EntityDiscussionThread> foundThread = repository.getThread(thread.getId());
        assertThat(foundThread.get().getComments(), hasItem(theComment));
    }

    @Test
    public void shouldCloseThread() {
        repository.setThreadStatus(thread.getId(), Status.CLOSED);
        Optional<EntityDiscussionThread> readThread = repository.getThread(thread.getId());
        assertThat(readThread.get().getStatus(), is(Status.CLOSED));
    }

    @Test
    public void shouldUpdateComment() {
        String updatedBody = "The updated body";
        Comment updatedComment = new Comment(comment.getId(), comment.getCreatedBy(), comment.getCreatedAt(), Optional.of(44L), updatedBody, updatedBody);
        repository.updateComment(thread.getId(), updatedComment);
        Optional<EntityDiscussionThread> t = repository.getThread(thread.getId());
        assertThat(t.isPresent(), is(true));
        t.ifPresent(updatedThread -> assertThat(updatedThread.getComments(), hasItem(updatedComment)));
    }

    @Test
    public void shouldFindThreadByCommentId() {
        Optional<EntityDiscussionThread> foundThread = repository.findThreadByCommentId(comment.getId());
        assertThat(foundThread, is(Optional.of(thread)));
    }

    @Test
    public void shouldDeleteCommentById() {
        repository.deleteComment(comment.getId());
        Optional<EntityDiscussionThread> updatedThread = repository.getThread(thread.getId());
        assertThat(updatedThread.isPresent(), is(true));
        assertThat(updatedThread.get().getComments(), not(hasItem(comment)));
    }

    @Test
    public void shouldReplaceEntity() {
        OWLEntity theReplacement = MockingUtils.mockOWLClass();
        repository.replaceEntity(projectId,
                                 entity,
                                 theReplacement);

        List<EntityDiscussionThread> threads = repository.findThreads(projectId, theReplacement);
        assertThat(threads.size(), is(1));

    }

    @Test
    public void shouldGetThreadsInProject() {
        List<EntityDiscussionThread> threads = repository.getThreadsInProject(projectId);
        assertThat(threads.size(), is(1));
    }

    @Test
    public void shouldGetCommentsCount() {
        int count = repository.getCommentsCount(projectId, entity);
        assertThat(count, is(1));
    }

    @Test
    public void shouldGetOpenCommentsCount() {
        int count = repository.getOpenCommentsCount(projectId, entity);
        assertThat(count, is(1));
    }

    private MongoCollection<Document> getCollection() {
        return mongoTemplate.getCollection("EntityDiscussionThreads");
    }



}
