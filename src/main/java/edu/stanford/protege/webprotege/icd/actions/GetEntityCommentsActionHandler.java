package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.icd.dtos.*;
import edu.stanford.protege.webprotege.icd.mappers.EntityCommentThreadMapper;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.issues.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class GetEntityCommentsActionHandler extends AbstractProjectActionHandler<GetEntityCommentsAction, GetEntityCommentsResponse> {

    @Nonnull
    private final EntityDiscussionThreadRepository repository;


    public GetEntityCommentsActionHandler(@Nonnull EntityDiscussionThreadRepository repository,
                                          @Nonnull AccessManager accessManager) {
        super(accessManager);
        this.repository = repository;
    }

    @NotNull
    @Override
    public Class<GetEntityCommentsAction> getActionClass() {
        return GetEntityCommentsAction.class;
    }

    @NotNull
    @Override
    public GetEntityCommentsResponse execute(@NotNull GetEntityCommentsAction action, @NotNull ExecutionContext executionContext) {
        var owlEntity = DataFactory.getOWLClass(action.entityIri());
        List<EntityDiscussionThread> threads = repository.findThreads(action.projectId(), owlEntity);
        List<EntityCommentThread> comments = threads.stream()
                .map(EntityCommentThreadMapper::mapFromDiscussionThread)
                .toList();
        return GetEntityCommentsResponse.create(EntityComments.create(comments));
    }
}
