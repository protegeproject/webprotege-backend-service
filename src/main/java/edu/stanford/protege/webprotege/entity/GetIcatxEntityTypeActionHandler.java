package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.*;

public class GetIcatxEntityTypeActionHandler extends AbstractProjectActionHandler<GetIcatxEntityTypeAction, GetIcatxEntityTypeResult> {

    private final ClassHierarchyProvider classHierarchyProvider;

    private final IcatxEntityTypeConfigurationRepository repository;


    public GetIcatxEntityTypeActionHandler(@NotNull AccessManager accessManager,
                                           ClassHierarchyProvider classHierarchyProvider,
                                           IcatxEntityTypeConfigurationRepository repository) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.repository = repository;
    }

    @NotNull
    @Override
    public Class<GetIcatxEntityTypeAction> getActionClass() {
        return GetIcatxEntityTypeAction.class;
    }

    @NotNull
    @Override
    public GetIcatxEntityTypeResult execute(@NotNull GetIcatxEntityTypeAction action, @NotNull ExecutionContext executionContext) {
        List<IcatxEntityTypeConfiguration> configurations = repository.getAllConfigurations();

        List<IRI> ancestorsIris = classHierarchyProvider.getAncestors(new OWLClassImpl(action.entityIri())).stream()
                .map(OWLClass::getIRI).toList();

        List<IcatxEntityTypeConfiguration> matchingParents = configurations.stream()
                .filter(config -> ancestorsIris.contains(config.topLevelIri())).toList();

        List<String> excludedTypes = matchingParents.stream().map(IcatxEntityTypeConfiguration::excludesEntityType)
                .filter(Objects::nonNull)
                .toList();

        List<String> response = matchingParents.stream()
                .map(IcatxEntityTypeConfiguration::icatxEntityType)
                .filter(o -> !excludedTypes.contains(o))
                .toList();

        return new GetIcatxEntityTypeResult(response);
    }
}
