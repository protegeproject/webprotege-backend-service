package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@WebProtegeHandler
public class GetProjectHierarchyDescriptorRulesCommandHandler implements AuthorizedCommandHandler<GetProjectHierarchyDescriptorRulesRequest, GetProjectHierarchyDescriptorRulesResponse> {

    private final HierarchyDescriptorRulesRepository repository;

    public GetProjectHierarchyDescriptorRulesCommandHandler(@NotNull AccessManager accessManager, HierarchyDescriptorRulesRepository repository) {
        this.repository = repository;
    }

    @NotNull
    @Override
    public Resource getTargetResource(GetProjectHierarchyDescriptorRulesRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @NotNull
    @Override
    public Collection<Capability> getRequiredCapabilities() {
        return List.of(BuiltInCapability.EDIT_PROJECT_SETTINGS.getCapability());
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectHierarchyDescriptorRulesRequest.CHANNEL;
    }

    @Override
    public Class<GetProjectHierarchyDescriptorRulesRequest> getRequestClass() {
        return GetProjectHierarchyDescriptorRulesRequest.class;
    }

    @Override
    public Mono<GetProjectHierarchyDescriptorRulesResponse> handleRequest(GetProjectHierarchyDescriptorRulesRequest request, ExecutionContext executionContext) {
        var rules = repository.find(request.projectId());
        var response = new GetProjectHierarchyDescriptorRulesResponse(rules.map(ProjectHierarchyDescriptorRules::rules).orElse(List.of()));
        return Mono.just(response);
    }

}
