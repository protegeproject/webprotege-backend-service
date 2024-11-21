package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class FilterExistingEntitiesActionHandler extends AbstractProjectActionHandler<FilterExistingEntitiesAction, FilterExistingEntitiesResult> {

    private final ClassHierarchyProvider classHierarchyProvider;

    public FilterExistingEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
    }

    @Nonnull
    @Override
    public Class<FilterExistingEntitiesAction> getActionClass() {
        return FilterExistingEntitiesAction.class;
    }

    @Nonnull
    @Override
    public FilterExistingEntitiesResult execute(@Nonnull FilterExistingEntitiesAction action, @Nonnull ExecutionContext executionContext) {
        var existingIris = action.iris().stream()
                .filter(iri -> classHierarchyProvider.contains(DataFactory.getOWLClass(iri)))
                .collect(Collectors.toSet());
        return FilterExistingEntitiesResult.create(existingIris);
    }
}
