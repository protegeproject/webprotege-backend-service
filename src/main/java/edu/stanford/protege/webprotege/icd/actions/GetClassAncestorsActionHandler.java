package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.hierarchy.*;
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

    public GetClassAncestorsActionHandler(@NotNull AccessManager accessManager,
                                          RenderingManager renderingManager,
                                          @Nonnull HierarchyProviderManager hierarchyProviderMapper,
                                          IcatxEntityTypeConfigurationRepository repository) {
        super(accessManager);
        this.hierarchyProviderMapper = hierarchyProviderMapper;
        this.renderingManager = renderingManager;
        this.repository = repository;
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
                .filter(parent -> !excludedTypes.contains(parent.excludesEntityType()))
                .map(parent -> DataFactory.getOWLClass(parent.topLevelIri()))
                .collect(Collectors.toSet());

        ClassHierarchyDescriptor classDescriptorWithRoots = ClassHierarchyDescriptor.create(entityTypesRoots);
        return hierarchyProviderMapper.getHierarchyProvider(classDescriptorWithRoots)
                .map(hierarchyProviderMapper -> {
                    var ancestors = hierarchyProviderMapper.getAncestors(DataFactory.getOWLClass(action.classIri()))
                            .stream()
                            .filter(ancestor -> ancestor.isOWLClass()&&!entityTypesRoots.contains(ancestor))
                            .map(renderingManager::getRendering)
                            .collect(Collectors.toSet());
                    return new GetClassAncestorsResult(ancestors);
                }).orElse(new GetClassAncestorsResult(Collections.emptySet()));

        // de păstrat ordinea părinților
    }
}

