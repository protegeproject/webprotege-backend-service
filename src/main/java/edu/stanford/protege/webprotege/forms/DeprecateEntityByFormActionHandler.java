package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.projectsettings.EntityDeprecationSettings;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
public class DeprecateEntityByFormActionHandler extends AbstractProjectChangeHandler<OWLEntity, DeprecateEntityByFormAction, DeprecateEntityByFormResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DeprecateEntityByFormChangeListGeneratorFactory changeListGeneratorFactory;

    @Nonnull
    private final ProjectDetailsRepository projectDetailsRepository;

    @Inject
    public DeprecateEntityByFormActionHandler(@Nonnull AccessManager accessManager,

                                              @Nonnull HasApplyChanges applyChanges, @Nonnull ProjectId projectId, @Nonnull DeprecateEntityByFormChangeListGeneratorFactory changeListGeneratorFactory,
                                              @Nonnull ProjectDetailsRepository projectDetailsRepository) {
        super(accessManager, applyChanges);
        this.projectId = projectId;
        this.changeListGeneratorFactory = changeListGeneratorFactory;
        this.projectDetailsRepository = projectDetailsRepository;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(DeprecateEntityByFormAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public Class<DeprecateEntityByFormAction> getActionClass() {
        return DeprecateEntityByFormAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(DeprecateEntityByFormAction action,
                                                                  ExecutionContext executionContext) {

        var entityToBeDeprecated = action.getEntity();
        var entityDeprecationSettings = projectDetailsRepository.findOne(projectId)
                .map(ProjectDetails::getEntityDeprecationSettings)
                .orElse(EntityDeprecationSettings.empty());
        return changeListGeneratorFactory.create(action.changeRequestId(), entityToBeDeprecated,
                                                 action.getDeprecationFormData(),
                                                 action.getReplacementEntity(),
                                                 entityDeprecationSettings);
    }

    @Override
    protected DeprecateEntityByFormResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                             DeprecateEntityByFormAction action,
                                                             ExecutionContext executionContext) {
        return new DeprecateEntityByFormResult();
    }
}
