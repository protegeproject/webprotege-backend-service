package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ApplicationPermissionValidator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2017
 */
public class SetApplicationSettingsActionHandler implements ApplicationActionHandler<SetApplicationSettingsAction, SetApplicationSettingsResult> {

    private final ApplicationSettingsManager applicationSettingsManager;

    private final AccessManager accessManager;

    @Inject
    public SetApplicationSettingsActionHandler(@Nonnull ApplicationSettingsManager applicationSettingsManager,
                                               @Nonnull AccessManager accessManager) {
        this.applicationSettingsManager = checkNotNull(applicationSettingsManager);
        this.accessManager = checkNotNull(accessManager);
    }

    @Nonnull
    @Override
    public Class<SetApplicationSettingsAction> getActionClass() {
        return SetApplicationSettingsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull SetApplicationSettingsAction action, @Nonnull RequestContext requestContext) {
        return new ApplicationPermissionValidator(accessManager,
                                                  requestContext.getUserId(),
                                                  BuiltInAction.EDIT_APPLICATION_SETTINGS, requestContext.getExecutionContext());
    }

    @Nonnull
    @Override
    public SetApplicationSettingsResult execute(@Nonnull SetApplicationSettingsAction action, @Nonnull ExecutionContext executionContext) {
        applicationSettingsManager.setApplicationSettings(action.getApplicationSettings());
        return SetApplicationSettingsResult.create();
    }
}
