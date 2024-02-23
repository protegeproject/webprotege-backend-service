package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Feb 2018
 */
public class GetProjectPrefixDeclarationsActionHandler extends AbstractProjectActionHandler<GetProjectPrefixDeclarationsRequest, GetProjectPrefixDeclarationsResponse> {

    @Nonnull
    private final PrefixDeclarationsStore store;

    @Inject
    public GetProjectPrefixDeclarationsActionHandler(@Nonnull AccessManager accessManager,
                                                     @Nonnull PrefixDeclarationsStore store) {
        super(accessManager);
        this.store = checkNotNull(store);
    }

    @Nonnull
    @Override
    public Class<GetProjectPrefixDeclarationsRequest> getActionClass() {
        return GetProjectPrefixDeclarationsRequest.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectPrefixDeclarationsRequest action) {
        return BuiltInAction.EDIT_PROJECT_PREFIXES;
    }

    @Nonnull
    @Override
    public GetProjectPrefixDeclarationsResponse execute(@Nonnull GetProjectPrefixDeclarationsRequest action,
                                                      @Nonnull ExecutionContext executionContext) {

        ProjectId projectId = action.projectId();
        var decls = store.find(projectId)
                                                      .getPrefixes()
                                                      .entrySet()
                                                      .stream()
                                                      .map(entry -> PrefixDeclaration.get(entry.getKey(), entry.getValue()))
                                                      .collect(toImmutableList());
        return new GetProjectPrefixDeclarationsResponse(projectId, decls);
    }
}
