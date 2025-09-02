package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.icd.mappers.AncestorHierarchyNodeMapper;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@JsonTypeName(GetLogicalDefinitionsClassAncestorsAction.CHANNEL)
public class GetLogicalDefinitionsClassAncestorsActionHandler extends AbstractProjectActionHandler<GetLogicalDefinitionsClassAncestorsAction, GetLogicalDefinitionsClassAncestorsResult> {


    private final RenderingManager renderingManager;

    @Nonnull
    private final HierarchyProviderManager hierarchyProviderMapper;

    private final IcatxEntityTypeConfigurationRepository repository;

    private final AncestorHierarchyNodeMapper ancestorHierarchyNodeMapper;

    public GetLogicalDefinitionsClassAncestorsActionHandler(@NotNull AccessManager accessManager,
                                                            RenderingManager renderingManager,
                                                            @Nonnull HierarchyProviderManager hierarchyProviderMapper,
                                                            IcatxEntityTypeConfigurationRepository repository,
                                                            AncestorHierarchyNodeMapper ancestorHierarchyNodeMapper) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.renderingManager = renderingManager;
        this.repository = repository;
        this.ancestorHierarchyNodeMapper = ancestorHierarchyNodeMapper;
    }

    @NotNull
    @Override
    public Class<GetLogicalDefinitionsClassAncestorsAction> getActionClass() {
        return GetLogicalDefinitionsClassAncestorsAction.class;
    }

    @NotNull
    @Override
    public GetLogicalDefinitionsClassAncestorsResult execute(@NotNull GetLogicalDefinitionsClassAncestorsAction action, @NotNull ExecutionContext executionContext) {
        List<IcatxEntityTypeConfiguration> configurations = repository.findAllByProjectId(action.projectId());

        Set<IRI> highLevelEntities = configurations.stream()
                .filter(config -> "".equals(config.excludesEntityType()))
                .map(IcatxEntityTypeConfiguration::topLevelIri)
                .collect(Collectors.toSet());

        List<IRI> ancestorsIris = new ArrayList<>(hierarchyProviderMapper.getHierarchyProvider(ClassHierarchyDescriptor.create()).get()
                .getAncestors(DataFactory.getOWLClass(action.classIri())))
                .stream()
                .map(OWLNamedObject::getIRI)
                .toList();

        List<IcatxEntityTypeConfiguration> matchingParents = configurations.stream()
                .filter(config -> ancestorsIris.contains(config.topLevelIri())).toList();

        List<String> excludedTypes = matchingParents.stream().map(IcatxEntityTypeConfiguration::excludesEntityType)
                .filter(Objects::nonNull)
                .toList();

        Set<OWLClass> entityTypesRoots = matchingParents.stream()
                .filter(parent -> !excludedTypes.contains(parent.icatxEntityType()))
                .map(parent -> DataFactory.getOWLClass(parent.topLevelIri()))
                .collect(Collectors.toSet());

        if(entityTypesRoots.isEmpty()) {
            return new GetLogicalDefinitionsClassAncestorsResult(getEmptyAncestorHierarchy(action.classIri()));
        }
        ClassHierarchyDescriptor classDescriptorWithRoots = ClassHierarchyDescriptor.create(entityTypesRoots);
        return hierarchyProviderMapper.getHierarchyProvider(classDescriptorWithRoots)
                .map(hierarchyProviderMapper -> {
                    var ancestorsTree = hierarchyProviderMapper.getAncestorsTree(DataFactory.getOWLClass(action.classIri()))
                            .map(this.ancestorHierarchyNodeMapper::map)
                            .orElse(getEmptyAncestorHierarchy(action.classIri()));

                    AncestorHierarchyNode<OWLEntityData> pruned = prune(ancestorsTree, highLevelEntities);

                    if (pruned == null) {
                        pruned = getEmptyAncestorHierarchy(action.classIri());
                    }
                    return new GetLogicalDefinitionsClassAncestorsResult(pruned);

                }).orElseGet(() -> new GetLogicalDefinitionsClassAncestorsResult(getEmptyAncestorHierarchy(action.classIri())));
    }

    private AncestorHierarchyNode<OWLEntityData> getEmptyAncestorHierarchy(IRI classIri){
        AncestorHierarchyNode<OWLEntityData> response = new AncestorHierarchyNode<>();
        response.setNode(renderingManager.getRendering(DataFactory.getOWLClass(classIri)));
        response.setChildren(Collections.emptyList());
        return response;
    }

    /**
     * Recursively drops any subtree whose node‐IRI appears in the highLevelEntities set.
     * Returns null if this node (and its entire branch) should be removed.
     */
    private AncestorHierarchyNode<OWLEntityData> prune(AncestorHierarchyNode<OWLEntityData> node,
                                                       Set<IRI> highLevelEntities) {
        IRI nodeIri = node.getNode().getEntity().getIRI();
        if (highLevelEntities.contains(nodeIri)) {
            // drop this node (and everything under it)
            return null;
        }
        // otherwise keep the node, but recurse into its children
        List<AncestorHierarchyNode<OWLEntityData>> keptKids = node.getChildren().stream()
                .map(child -> prune(child, highLevelEntities))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        node.setChildren(keptKids);
        return node;
    }
}

