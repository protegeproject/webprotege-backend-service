package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class SetUserProjectEntityGraphCriteriaActionHandler extends AbstractProjectActionHandler<SetUserProjectEntityGraphSettingsAction, SetUserProjectEntityGraphSettingsResult> {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final EntityGraphSettingsRepository repository;

    @Inject
    public SetUserProjectEntityGraphCriteriaActionHandler(@Nonnull AccessManager accessManager,
                                                          @Nonnull EntityGraphSettingsRepository repository) {
        super(accessManager);
        this.accessManager = accessManager;
        this.repository = checkNotNull(repository);
    }

    @Nonnull
    @Override
    public SetUserProjectEntityGraphSettingsResult execute(@Nonnull SetUserProjectEntityGraphSettingsAction action,
                                                   @Nonnull ExecutionContext executionContext) {

        if(action.getUserId()
                 .equals(Optional.of(UserId.getGuest()))) {
            return new SetUserProjectEntityGraphSettingsResult();
        }
        var settings = action.settings();
        var projectId = action.projectId();
        var userSettings = action.getUserId()
                                 .map(userId -> ProjectUserEntityGraphSettings.get(projectId, userId, settings))
                                 .orElse(
                                         ProjectUserEntityGraphSettings.get(projectId, null, settings)
                                 );
        repository.saveSettings(userSettings);
        return new SetUserProjectEntityGraphSettingsResult();
    }

    @Nonnull
    @Override
    public Class<SetUserProjectEntityGraphSettingsAction> getActionClass() {
        return SetUserProjectEntityGraphSettingsAction.class;
    }

    @Nonnull
    @Override
    protected RequestValidator getAdditionalRequestValidator(SetUserProjectEntityGraphSettingsAction action,
                                                             RequestContext requestContext) {
        return () -> {
            UserId userInSession = requestContext.getUserId();
            if(action.getUserId().equals(Optional.of(userInSession))) {
                // Users can set their own settings
                return RequestValidationResult.getValid();
            }
            else {
                if(accessManager.hasPermission(Subject.forUser(userInSession),
                                               ProjectResource.forProject(action.projectId()),
                                               BuiltInAction.EDIT_DEFAULT_VISUALIZATION_SETTINGS)) {
                    return RequestValidationResult.getValid();
                }
                else {
                    return RequestValidationResult.getInvalid("Permission denied");
                }
            }
        };

    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetUserProjectEntityGraphSettingsAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }
}
