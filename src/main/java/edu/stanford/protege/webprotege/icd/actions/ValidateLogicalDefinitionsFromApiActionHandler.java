package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.postcoordination.GetPostcoordinationAxisToGenericScaleRequest;
import edu.stanford.protege.webprotege.postcoordination.GetPostcoordinationAxisToGenericScaleResponse;
import edu.stanford.protege.webprotege.postcoordination.PostcoordinationAxisToGenericScale;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ValidateLogicalDefinitionsFromApiActionHandler extends AbstractProjectActionHandler<ValidateLogicalDefinitionsFromApiAction, ValidateLogicalDefinitionsFromApiResult> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ValidateLogicalDefinitionsFromApiActionHandler.class);

    private final CommandExecutor<GetPostcoordinationAxisToGenericScaleRequest, GetPostcoordinationAxisToGenericScaleResponse> getPostcoordinationAxisToGenericScaleExecutor;
    private final HierarchyProviderManager hierarchyProviderManager;
    private final ClassHierarchyProvider classHierarchyProvider;
    private final RenderingManager renderingManager;

    public ValidateLogicalDefinitionsFromApiActionHandler(
            @Nonnull AccessManager accessManager,
            @Nonnull CommandExecutor<GetPostcoordinationAxisToGenericScaleRequest, GetPostcoordinationAxisToGenericScaleResponse> getPostcoordinationAxisToGenericScaleExecutor,
            @Nonnull HierarchyProviderManager hierarchyProviderManager,
            @Nonnull ClassHierarchyProvider classHierarchyProvider,
            @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.getPostcoordinationAxisToGenericScaleExecutor = getPostcoordinationAxisToGenericScaleExecutor;
        this.hierarchyProviderManager = hierarchyProviderManager;
        this.classHierarchyProvider = classHierarchyProvider;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<ValidateLogicalDefinitionsFromApiAction> getActionClass() {
        return ValidateLogicalDefinitionsFromApiAction.class;
    }

    @Nonnull
    @Override
    public ValidateLogicalDefinitionsFromApiResult execute(@Nonnull ValidateLogicalDefinitionsFromApiAction action, @Nonnull ExecutionContext executionContext) {
        List<String> errorMessages = new ArrayList<>();

        try {
            // Get postcoordination axis to generic scale mapping
            GetPostcoordinationAxisToGenericScaleResponse axisToScaleResponse = getPostcoordinationAxisToGenericScaleExecutor
                    .execute(new GetPostcoordinationAxisToGenericScaleRequest(), executionContext)
                    .get(15, TimeUnit.SECONDS);

            Map<String, String> axisToGenericScaleMap = axisToScaleResponse.postcoordinationAxisToGenericScales().stream()
                    .collect(Collectors.toMap(
                            PostcoordinationAxisToGenericScale::getPostcoordinationAxis,
                            PostcoordinationAxisToGenericScale::getGenericPostcoordinationScaleTopClass
                    ));

            // Validate fillers in logical definitions relationships
            for (ValidateLogicalDefinitionsFromApiAction.LogicalDefinition logicalDef : action.logicalDefinitions()) {
                for (ValidateLogicalDefinitionsFromApiAction.Relationship relationship : logicalDef.relationships()) {
                    validateFillerInHierarchy(
                            relationship.axis(),
                            relationship.filler(),
                            axisToGenericScaleMap,
                            errorMessages
                    );
                }
            }

            // Validate fillers in necessary conditions
            for (ValidateLogicalDefinitionsFromApiAction.Relationship relationship : action.necessaryConditions()) {
                validateFillerInHierarchy(
                        relationship.axis(),
                        relationship.filler(),
                        axisToGenericScaleMap,
                        errorMessages
                );
            }

            // Validate logical definition superclasses are ancestors of entity
            OWLClass entityClass = DataFactory.getOWLClass(IRI.create(action.entityIri()));
            for (ValidateLogicalDefinitionsFromApiAction.LogicalDefinition logicalDef : action.logicalDefinitions()) {
                OWLClass superclassClass = DataFactory.getOWLClass(IRI.create(logicalDef.logicalDefinitionSuperclass()));
                
                if (!classHierarchyProvider.isAncestor(entityClass, superclassClass)) {
                    String entityBrowserText = renderingManager.getShortForm(entityClass);
                    String superclassBrowserText = renderingManager.getShortForm(superclassClass);
                    errorMessages.add(String.format(
                            "Logical definition superclass '%s' is not an ancestor of entity '%s'",
                            superclassBrowserText,
                            entityBrowserText
                    ));
                }
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            errorMessages.add("Error during validation: " + e.getMessage());
        }

        return ValidateLogicalDefinitionsFromApiResult.create(errorMessages);
    }

    private void validateFillerInHierarchy(
            String axisIri,
            String fillerIri,
            Map<String, String> axisToGenericScaleMap,
            List<String> errorMessages) {
        
        String genericPostcoordinationScaleTopClassIri = axisToGenericScaleMap.get(axisIri);
        
        if (genericPostcoordinationScaleTopClassIri == null) {
            // Axis not found in mapping - skip validation for this axis
            errorMessages.add("Axis not available: " + axisIri);
            return;
        }

        try {
            OWLClass rootClass = DataFactory.getOWLClass(IRI.create(genericPostcoordinationScaleTopClassIri));
            ClassHierarchyDescriptor hierarchyDescriptor = ClassHierarchyDescriptor.create(Set.of(rootClass));
            
            Optional<HierarchyProvider<OWLEntity>> hierarchyProviderOpt = hierarchyProviderManager.getHierarchyProvider(hierarchyDescriptor);
            
            if (hierarchyProviderOpt.isEmpty()) {
                errorMessages.add("Root class not found for: " + genericPostcoordinationScaleTopClassIri);
                return;
            }

            HierarchyProvider<OWLEntity> hierarchyProvider = hierarchyProviderOpt.get();
            
            // Get all entities in the hierarchy (root + descendants)
            Set<OWLEntity> hierarchyEntities = new HashSet<>();
            hierarchyEntities.add(rootClass);
            hierarchyEntities.addAll(hierarchyProvider.getDescendants(rootClass));
            
            OWLClass fillerClass = DataFactory.getOWLClass(IRI.create(fillerIri));
            
            if (!hierarchyEntities.contains(fillerClass)) {
                String fillerBrowserText = renderingManager.getShortForm(fillerClass);
                String axisBrowserText = renderingManager.getShortForm(DataFactory.getOWLClass(IRI.create(axisIri)));
                String rootBrowserText = renderingManager.getShortForm(rootClass);
                
                errorMessages.add(String.format(
                        "Filler '%s' is not valid for axis '%s'. It must be a descendant of '%s'",
                        fillerBrowserText,
                        axisBrowserText,
                        rootBrowserText
                ));
            }
        } catch (Exception e) {
            LOGGER.error("Error validating from api",e);
        }
    }
}
