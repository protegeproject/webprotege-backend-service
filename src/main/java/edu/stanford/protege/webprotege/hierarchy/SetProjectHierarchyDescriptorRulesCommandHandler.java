package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@WebProtegeHandler
public class SetProjectHierarchyDescriptorRulesCommandHandler implements AuthorizedCommandHandler<SetProjectHierarchyDescriptorRulesRequest,SetProjectHierarchyDescriptorRulesResponse> {

    private final HierarchyDescriptorRulesRepository repository;

    public SetProjectHierarchyDescriptorRulesCommandHandler(HierarchyDescriptorRulesRepository repository) {
        this.repository = repository;
    }

    @NotNull
    @Override
    public Resource getTargetResource(SetProjectHierarchyDescriptorRulesRequest request) {
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
        return SetProjectHierarchyDescriptorRulesRequest.CHANNEL;
    }

    @Override
    public Class<SetProjectHierarchyDescriptorRulesRequest> getRequestClass() {
        return SetProjectHierarchyDescriptorRulesRequest.class;
    }

    @Override
    public Mono<SetProjectHierarchyDescriptorRulesResponse> handleRequest(SetProjectHierarchyDescriptorRulesRequest request, ExecutionContext executionContext) {
        repository.save(new ProjectHierarchyDescriptorRules(request.projectId(), request.rules()));
        return Mono.just(new SetProjectHierarchyDescriptorRulesResponse());
    }
}
