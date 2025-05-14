package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.crud.DeleteEntitiesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import jakarta.inject.Inject;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
public class DeleteEntitiesActionHandler extends AbstractProjectChangeHandler<Set<OWLEntity>, DeleteEntitiesAction, DeleteEntitiesResult> {

    @Nonnull
    private final DeleteEntitiesChangeListGeneratorFactory factory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public DeleteEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull DeleteEntitiesChangeListGeneratorFactory factory,
                                       @Nonnull RenderingManager renderingManager) {
        super(accessManager, applyChanges);
        this.factory = factory;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<DeleteEntitiesAction> getActionClass() {
        return DeleteEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(DeleteEntitiesAction action) {
        return BuiltInCapability.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Set<OWLEntity>> getChangeListGenerator(DeleteEntitiesAction action,
                                                                         ExecutionContext executionContext) {
        var owlEntities = action.entities().stream().map(OWLEntityData::getEntity).collect(Collectors.toSet());
        return factory.create(owlEntities, action.changeRequestId());
    }

    @Override
    protected DeleteEntitiesResult createActionResult(ChangeApplicationResult<Set<OWLEntity>> changeApplicationResult,
                                                      DeleteEntitiesAction action,
                                                      ExecutionContext executionContext) {
        var entitiesData = changeApplicationResult.getSubject()
                .stream()
                .map(renderingManager::getRendering)
                .collect(toImmutableSet());
        return new DeleteEntitiesResult(entitiesData);
    }
}
