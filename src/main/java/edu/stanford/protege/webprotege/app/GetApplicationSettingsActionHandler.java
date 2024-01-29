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
 * 16 Mar 2017
 */
public class GetApplicationSettingsActionHandler implements ApplicationActionHandler<GetApplicationSettingsAction, GetApplicationSettingsResult> {

    private final AccessManager manager;

    private final ApplicationSettingsManager applicationSettingsManager;

    @Inject
    public GetApplicationSettingsActionHandler(@Nonnull AccessManager manager,
                                               @Nonnull ApplicationSettingsManager applicationSettingsManager) {
        this.manager = checkNotNull(manager);
        this.applicationSettingsManager = checkNotNull(applicationSettingsManager);
    }

    @Nonnull
    @Override
    public Class<GetApplicationSettingsAction> getActionClass() {
        return GetApplicationSettingsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetApplicationSettingsAction action, @Nonnull RequestContext requestContext) {
        return new ApplicationPermissionValidator(manager,
                                                  requestContext.getUserId(),
                                                  BuiltInAction.EDIT_APPLICATION_SETTINGS, requestContext.getExecutionContext());
    }

    @Nonnull
    @Override
    public GetApplicationSettingsResult execute(@Nonnull GetApplicationSettingsAction action, @Nonnull ExecutionContext executionContext) {
        return new GetApplicationSettingsResult(applicationSettingsManager.getApplicationSettings());
    }
}
