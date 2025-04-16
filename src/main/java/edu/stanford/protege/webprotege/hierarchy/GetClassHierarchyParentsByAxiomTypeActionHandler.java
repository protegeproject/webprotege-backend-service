package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.*;
import javax.inject.Inject;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

public class GetClassHierarchyParentsByAxiomTypeActionHandler extends AbstractProjectActionHandler<GetClassHierarchyParentsByAxiomTypeAction, GetClassHierarchyParentsByAxiomTypeResult> {

    @Nonnull
    private final ClassHierarchyProviderFactory classHierarchyFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetClassHierarchyParentsByAxiomTypeActionHandler(@Nonnull AccessManager accessManager,
                                                            @Nonnull ClassHierarchyProviderFactory classHierarchyFactory,
                                                            @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.classHierarchyFactory = classHierarchyFactory;
        this.renderingManager = renderingManager;
    }

    @Nonnull
    @Override
    public Class<GetClassHierarchyParentsByAxiomTypeAction> getActionClass() {
        return GetClassHierarchyParentsByAxiomTypeAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetClassHierarchyParentsByAxiomTypeAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetClassHierarchyParentsByAxiomTypeResult execute(@Nonnull GetClassHierarchyParentsByAxiomTypeAction action, @Nonnull ExecutionContext executionContext) {
        var roots = action.classHierarchyDescriptor().roots();
        var owlClass = action.owlClass();
        var classHierarchyProvider = classHierarchyFactory.getClassHierarchyProvider(roots);
        var parentsBySubClassOf = classHierarchyProvider.getParentsStreamFromSubClassOf(owlClass)
                .filter(parent -> !parent.isTopEntity())
                .map(renderingManager::getRendering)
                .collect(Collectors.toSet());
        var parentsByEquivalentClass = classHierarchyProvider.getParentsStreamFromEquivalentClass(owlClass)
                .filter(parent -> !parent.isTopEntity())
                .map(renderingManager::getRendering)
                .collect(Collectors.toSet());

        return new GetClassHierarchyParentsByAxiomTypeResult(owlClass, parentsBySubClassOf, parentsByEquivalentClass);
    }
}
