package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetNamedHierarchyActionHandler extends AbstractProjectActionHandler<SetNamedHierarchiesAction, SetNamedHierarchiesResponse> {

    private final NamedHierarchyManager namedHierarchyManager;

    public SetNamedHierarchyActionHandler(@NotNull AccessManager accessManager, NamedHierarchyManager namedHierarchyManager) {
        super(accessManager);
        this.namedHierarchyManager = namedHierarchyManager;
    }

    @NotNull
    @Override
    public Class<SetNamedHierarchiesAction> getActionClass() {
        return SetNamedHierarchiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(SetNamedHierarchiesAction action) {
        return BuiltInCapability.CONFIGURE_HIERARCHIES;
    }

    @NotNull
    @Override
    public SetNamedHierarchiesResponse execute(@NotNull SetNamedHierarchiesAction action, @NotNull ExecutionContext executionContext) {
        namedHierarchyManager.setNamedHierarchies(action.projectId(), action.namedHierarchies());
        return new SetNamedHierarchiesResponse();
    }
}
