package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.forms.CreateEntityFromFormDataAction;
import edu.stanford.protege.webprotege.forms.CreateEntityFromFormDataResult;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-01
 */
public class CreateEntityFromFormDataActionHandler extends AbstractProjectChangeHandler<OWLEntity, CreateEntityFromFormDataAction, CreateEntityFromFormDataResult> {

    private final CreateEntityFromFormDataChangeListGeneratorFactory changeListGeneratorFactory;

    private final RenderingManager renderer;

    @Inject
    public CreateEntityFromFormDataActionHandler(@Nonnull AccessManager accessManager,

                                                 @Nonnull HasApplyChanges applyChanges,
                                                 @Nonnull CreateEntityFromFormDataChangeListGeneratorFactory changeListGeneratorFactory,
                                                 @Nonnull RenderingManager renderer) {
        super(accessManager, applyChanges);
        this.changeListGeneratorFactory = checkNotNull(changeListGeneratorFactory);
        this.renderer = renderer;
    }

    @Nonnull
    @Override
    public Class<CreateEntityFromFormDataAction> getActionClass() {
        return CreateEntityFromFormDataAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CreateEntityFromFormDataAction action) {
        if(action.entityType().equals(EntityType.CLASS)) {
            return BuiltInAction.CREATE_CLASS;
        }
        else if(action.entityType().equals(EntityType.OBJECT_PROPERTY)) {
            return BuiltInAction.CREATE_PROPERTY;
        }
        else if(action.entityType().equals(EntityType.DATA_PROPERTY)) {
            return BuiltInAction.CREATE_PROPERTY;
        }
        else if(action.entityType().equals(EntityType.ANNOTATION_PROPERTY)) {
            return BuiltInAction.CREATE_PROPERTY;
        }
        else if(action.entityType().equals(EntityType.NAMED_INDIVIDUAL)) {
            return BuiltInAction.CREATE_INDIVIDUAL;
        }
        else if (action.entityType().equals(EntityType.DATATYPE)) {
            return BuiltInAction.CREATE_DATATYPE;
        }
        else {
            throw new RuntimeException("Unknown entity");
        }
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(CreateEntityFromFormDataAction action,
                                                                    ExecutionContext executionContext) {
        return changeListGeneratorFactory.create(action.changeRequestId(),
                                                 action.entityType(),
                                                 action.freshEntityIri(),
                                                 action.formData());
    }

    @Override
    protected CreateEntityFromFormDataResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                                CreateEntityFromFormDataAction action,
                                                                ExecutionContext executionContext) {

        var entityNodes = ImmutableSet.of(renderer.getRendering(changeApplicationResult.getSubject()));
        return new CreateEntityFromFormDataResult(action.projectId(),
                                                  entityNodes);
    }

}
