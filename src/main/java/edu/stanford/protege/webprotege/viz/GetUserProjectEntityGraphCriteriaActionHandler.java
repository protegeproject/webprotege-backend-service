package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class GetUserProjectEntityGraphCriteriaActionHandler extends AbstractProjectActionHandler<GetUserProjectEntityGraphCriteriaAction, GetUserProjectEntityGraphCriteriaResult> {

    private final EntityGraphSettingsRepository repository;

    @Inject
    public GetUserProjectEntityGraphCriteriaActionHandler(@Nonnull AccessManager accessManager,
                                                          EntityGraphSettingsRepository repository) {
        super(accessManager);
        this.repository = checkNotNull(repository);
    }

    @Nonnull
    @Override
    public Class<GetUserProjectEntityGraphCriteriaAction> getActionClass() {
        return GetUserProjectEntityGraphCriteriaAction.class;
    }

    @Nonnull
    @Override
    public GetUserProjectEntityGraphCriteriaResult execute(@Nonnull GetUserProjectEntityGraphCriteriaAction action,
                                                           @Nonnull ExecutionContext executionContext) {

        var projectId = action.projectId();
        var userId = executionContext.userId();
        var settings = repository.getSettingsForUserOrProjectDefault(projectId, userId);
        return new GetUserProjectEntityGraphCriteriaResult(projectId,
                                                           userId,
                                                           settings.getSettings());
    }
}
