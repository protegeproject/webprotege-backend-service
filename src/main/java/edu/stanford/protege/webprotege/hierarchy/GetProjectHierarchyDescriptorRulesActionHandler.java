package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetProjectHierarchyDescriptorRulesActionHandler extends AbstractProjectActionHandler<GetProjectHierarchyDescriptorRulesRequest, GetProjectHierarchyDescriptorRulesResponse> {

    private final HierarchyDescriptorRulesRepository repository;

    public GetProjectHierarchyDescriptorRulesActionHandler(@NotNull AccessManager accessManager, HierarchyDescriptorRulesRepository repository) {
        super(accessManager);
        this.repository = repository;
    }

    @NotNull
    @Override
    public Class<GetProjectHierarchyDescriptorRulesRequest> getActionClass() {
        return GetProjectHierarchyDescriptorRulesRequest.class;
    }

    @Nullable
    @Override
    protected ActionId getRequiredExecutableAction() {
        return BuiltInAction.EDIT_PROJECT_SETTINGS.getActionId();
    }

    @NotNull
    @Override
    public GetProjectHierarchyDescriptorRulesResponse execute(@NotNull GetProjectHierarchyDescriptorRulesRequest request, @NotNull ExecutionContext executionContext) {
        var rules = repository.find(request.projectId());
        return new GetProjectHierarchyDescriptorRulesResponse(rules.map(ProjectHierarchyDescriptorRules::rules).orElse(List.of()));
    }
}
