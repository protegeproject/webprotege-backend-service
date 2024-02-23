package edu.stanford.protege.webprotege.search;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class GetSearchSettingsActionHandler extends AbstractProjectActionHandler<GetSearchSettingsAction, GetSearchSettingsResult> {

    @Nonnull
    private final EntitySearchFilterRepository searchFilterRepository;

    @Inject
    public GetSearchSettingsActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull EntitySearchFilterRepository searchFilterRepository) {
        super(accessManager);
        this.searchFilterRepository = checkNotNull(searchFilterRepository);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetSearchSettingsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public Class<GetSearchSettingsAction> getActionClass() {
        return GetSearchSettingsAction.class;
    }

    @Nonnull
    @Override
    public GetSearchSettingsResult execute(@Nonnull GetSearchSettingsAction action,
                                           @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var filters = searchFilterRepository.getSearchFilters(projectId);
        return new GetSearchSettingsResult(filters);
    }
}
