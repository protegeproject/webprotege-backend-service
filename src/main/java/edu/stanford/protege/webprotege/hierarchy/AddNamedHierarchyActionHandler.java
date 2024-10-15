package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AddNamedHierarchyActionHandler extends AbstractProjectActionHandler<AddNamedHierarchyAction, AddNamedHierarchyResponse> {

    private final NamedHierarchyManager namedHierarchyManager;

    public AddNamedHierarchyActionHandler(@NotNull AccessManager accessManager, NamedHierarchyManager namedHierarchyManager) {
        super(accessManager);
        this.namedHierarchyManager = namedHierarchyManager;
    }

    @NotNull
    @Override
    public Class<AddNamedHierarchyAction> getActionClass() {
        return AddNamedHierarchyAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(AddNamedHierarchyAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @NotNull
    @Override
    public AddNamedHierarchyResponse execute(@NotNull AddNamedHierarchyAction action, @NotNull ExecutionContext executionContext) {
        var pd = namedHierarchyManager.getHierarchies(null);

        var hierarchyId = HierarchyId.get(UUID.randomUUID().toString());
        var namedHierarchy = new NamedHierarchy(hierarchyId,
                action.label(),
                action.description(),
                action.hierarchyDescriptor());
        namedHierarchyManager.saveHierarchy(action.projectId(), namedHierarchy);


        return new AddNamedHierarchyResponse();
    }
}
