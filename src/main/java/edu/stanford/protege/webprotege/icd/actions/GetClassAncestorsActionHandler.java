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

@JsonTypeName("webprotege.entities.GetClassAncestors")
public class GetClassAncestorsActionHandler extends AbstractProjectActionHandler<GetClassAncestorsAction, GetClassAncestorsResult> {


    private final RenderingManager renderingManager;

    @Nonnull
    private final HierarchyProviderManager hierarchyProviderMapper;

    private final IcatxEntityTypeConfigurationRepository repository;

    private final AncestorHierarchyNodeMapper ancestorHierarchyNodeMapper;

    public GetClassAncestorsActionHandler(@NotNull AccessManager accessManager,
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
    public Class<GetClassAncestorsAction> getActionClass() {
        return GetClassAncestorsAction.class;
    }

    @NotNull
    @Override
    public GetClassAncestorsResult execute(@NotNull GetClassAncestorsAction action, @NotNull ExecutionContext executionContext) {
        List<IcatxEntityTypeConfiguration> configurations = repository.findAllByProjectId(action.projectId());


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

        ClassHierarchyDescriptor classDescriptorWithRoots = ClassHierarchyDescriptor.create(entityTypesRoots);
        return hierarchyProviderMapper.getHierarchyProvider(classDescriptorWithRoots)
                .map(hierarchyProviderMapper -> {
                    var ancestorsTree = hierarchyProviderMapper.getAncestorsTree(DataFactory.getOWLClass(action.classIri()));
                    var ancestors =this.ancestorHierarchyNodeMapper.map(ancestorsTree);
                    return new GetClassAncestorsResult(ancestors);
                }).orElseGet(() -> {
                    AncestorHierarchyNode<OWLEntityData> response = new AncestorHierarchyNode<>();
                    response.setNode(renderingManager.getRendering(DataFactory.getOWLClass(action.classIri())));
                    response.setChildren(Collections.emptyList());
                    return new GetClassAncestorsResult(response);
                });
    }
}

