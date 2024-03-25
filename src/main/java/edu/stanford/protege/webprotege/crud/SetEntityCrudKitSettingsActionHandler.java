package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.FindAndReplaceIRIPrefixChangeGeneratorFactory;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_NEW_ENTITY_SETTINGS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SetEntityCrudKitSettingsActionHandler extends AbstractProjectActionHandler<SetEntityCrudKitSettingsAction, SetEntityCrudKitSettingsResult> {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectEntityCrudKitSettingsRepository repository;

    @Nonnull
    private final HasApplyChanges changeManager;

    @Nonnull
    private final FindAndReplaceIRIPrefixChangeGeneratorFactory findAndReplaceIRIPrefixChangeGeneratorFactory;

    @Inject
    public SetEntityCrudKitSettingsActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull ProjectId projectId,
                                                 @Nonnull ProjectEntityCrudKitSettingsRepository repository,
                                                 @Nonnull HasApplyChanges changeManager,
                                                 @Nonnull FindAndReplaceIRIPrefixChangeGeneratorFactory findAndReplaceIRIPrefixChangeGeneratorFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.repository = repository;
        this.changeManager = changeManager;
        this.findAndReplaceIRIPrefixChangeGeneratorFactory = findAndReplaceIRIPrefixChangeGeneratorFactory;

    }

    @Nonnull
    public Class<SetEntityCrudKitSettingsAction> getActionClass() {
        return SetEntityCrudKitSettingsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityCrudKitSettingsAction action) {
        return EDIT_NEW_ENTITY_SETTINGS;
    }

    @Nonnull
    @Override
    public SetEntityCrudKitSettingsResult execute(@Nonnull SetEntityCrudKitSettingsAction action,
                                                  @Nonnull ExecutionContext executionContext) {
        var projectSettings = ProjectEntityCrudKitSettings.get(projectId, action.toSettings());
        repository.save(projectSettings);
        if(action.prefixUpdateStrategy() == IRIPrefixUpdateStrategy.FIND_AND_REPLACE) {
            var fromPrefix = action.fromSettings().getPrefixSettings().getIRIPrefix();
            var toPrefix = action.toSettings().getPrefixSettings().getIRIPrefix();
            var changeGenerator = findAndReplaceIRIPrefixChangeGeneratorFactory.create(action.changeRequestId(),
                                                                                       fromPrefix, toPrefix);
            changeManager.applyChanges(executionContext.userId(), changeGenerator);
        }
        return new SetEntityCrudKitSettingsResult();
    }

}
