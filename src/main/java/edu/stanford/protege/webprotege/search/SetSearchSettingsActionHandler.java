package edu.stanford.protege.webprotege.search;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-17
 */
public class SetSearchSettingsActionHandler extends AbstractProjectActionHandler<SetSearchSettingsAction, SetSearchSettingsResult> {

    @Nonnull
    private final ProjectEntitySearchFiltersManager filtersManager;

    @Inject
    public SetSearchSettingsActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectEntitySearchFiltersManager filtersManager) {
        super(accessManager);
        this.filtersManager = checkNotNull(filtersManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetSearchSettingsAction action) {
        return BuiltInAction.EDIT_PROJECT_SETTINGS;
    }

    @Nonnull
    @Override
    public Class<SetSearchSettingsAction> getActionClass() {
        return SetSearchSettingsAction.class;
    }

    @Nonnull
    @Override
    public synchronized SetSearchSettingsResult execute(@Nonnull SetSearchSettingsAction action,
                                                        @Nonnull ExecutionContext executionContext) {

        var searchFilters = action.to();
        filtersManager.setSearchFilters(searchFilters);
        return new SetSearchSettingsResult();
    }
}
