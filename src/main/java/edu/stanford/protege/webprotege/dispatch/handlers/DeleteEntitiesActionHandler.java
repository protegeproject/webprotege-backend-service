package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.bulkop.ClassDeletedEvent;
import edu.stanford.protege.webprotege.bulkop.ParentsChangedEvent;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.crud.DeleteEntitiesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyDescriptor;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProvider;
import edu.stanford.protege.webprotege.hierarchy.HierarchyProviderManager;
import edu.stanford.protege.webprotege.icd.LinearizationParentChecker;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import jakarta.inject.Inject;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semarglproject.vocab.OWL;

import javax.annotation.*;
import java.util.*;
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

    private final HierarchyProviderManager hierarchyProviderManager;

    private final EventDispatcher eventDispatcher;

    private final LinearizationParentChecker linearizationParentChecker;

    @Inject
    public DeleteEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull DeleteEntitiesChangeListGeneratorFactory factory,
                                       @Nonnull RenderingManager renderingManager, HierarchyProviderManager hierarchyProviderManager, EventDispatcher eventDispatcher, LinearizationParentChecker linearizationParentChecker) {
        super(accessManager, applyChanges);
        this.factory = factory;
        this.renderingManager = renderingManager;
        this.hierarchyProviderManager = hierarchyProviderManager;
        this.eventDispatcher = eventDispatcher;
        this.linearizationParentChecker = linearizationParentChecker;
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
    @Nonnull
    protected RequestValidator getAdditionalRequestValidator(DeleteEntitiesAction action, RequestContext requestContext) {
        return () -> {
            HierarchyProvider<OWLEntity> hierarchyProvider = hierarchyProviderManager.getHierarchyProvider(ClassHierarchyDescriptor.create()).get();

            Map<OWLEntityData,List<OWLEntityData>> deletedEntityChildrenMap = new HashMap<>();

            for(OWLEntityData deletedEntity : action.entities()) {
                List<OWLEntityData> children = new ArrayList<>(hierarchyProvider.getChildren(deletedEntity.getEntity()).stream().map(renderingManager::getRendering).toList());
                deletedEntityChildrenMap.put(deletedEntity, children);
            }
            String childrenWithNoParentErrorMessage = "";
            for(OWLEntityData deletedEntity : deletedEntityChildrenMap.keySet()) {
                for(OWLEntityData child : deletedEntityChildrenMap.get(deletedEntity)) {
                    if(hierarchyProvider.getParents(child.getEntity()).size() == 1){
                        childrenWithNoParentErrorMessage += String.format("Child %s has only %s as parent\n", child.getBrowserText(), deletedEntity.getBrowserText());
                    }
                }
            }

            if(!childrenWithNoParentErrorMessage.isEmpty()) {
                return RequestValidationResult.getInvalid(childrenWithNoParentErrorMessage);
            }

            String childrenWithLinearizationParentErrorMessage = "";

            for(OWLEntityData deletedEntity : deletedEntityChildrenMap.keySet()) {
                for(OWLEntityData child : deletedEntityChildrenMap.get(deletedEntity)) {
                    Set<IRI> linearizationPathParents = linearizationParentChecker.getParentThatIsLinearizationPathParent(child.getEntity().getIRI(),
                            hierarchyProvider.getParents(child.getEntity()).stream().map(OWLEntity::getIRI).collect(Collectors.toSet()), action.projectId(), requestContext.getExecutionContext());
                        if(linearizationPathParents.contains(deletedEntity.getEntity().getIRI())){
                            childrenWithLinearizationParentErrorMessage += String.format("Child %s has %s as a linearization path parent\n", child.getBrowserText(), deletedEntity.getBrowserText());
                        }
                }
            }
            if(!childrenWithLinearizationParentErrorMessage.isEmpty()) {
                return RequestValidationResult.getInvalid(childrenWithLinearizationParentErrorMessage);
            }

            return NullValidator.get().validateAction();
        };
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
        HierarchyProvider<OWLEntity> hierarchyProvider = hierarchyProviderManager.getHierarchyProvider(ClassHierarchyDescriptor.create()).get();

        List<OWLEntity> impactedChildren = action.entities().stream().map(OWLEntityData::getEntity)
                .flatMap(entity -> hierarchyProvider.getChildren(entity).stream()).toList();

        var entitiesData = changeApplicationResult.getSubject()
                .stream()
                .map(renderingManager::getRendering)
                .collect(toImmutableSet());


        eventDispatcher.dispatchEvent(new ClassDeletedEvent(action.projectId(), EventId.generate(), action.entities().stream().map(entity -> entity.getEntity().getIRI()).toList()), executionContext);

        for(OWLEntity impactedChild : impactedChildren) {
            eventDispatcher.dispatchEvent(new ParentsChangedEvent(action.projectId(), EventId.generate(), impactedChild.getIRI()), executionContext);
        }

        return new DeleteEntitiesResult(entitiesData);
    }

}
