package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.HashMultimap;
import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.permissions.RebuildProjectPermissionsRequest;
import edu.stanford.protege.webprotege.permissions.RebuildProjectPermissionsResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebProtegeHandler
public class SetFormRegionAccessRestrictionsHandler implements AuthorizedCommandHandler<SetFormRegionAccessRestrictionsRequest, SetFormRegionAccessRestrictionsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(SetFormRegionAccessRestrictionsHandler.class);

    private final CommandExecutor<GetProjectRoleDefinitionsRequest, GetProjectRoleDefinitionsResponse> getRoleDefsExecutor;

    private final CommandExecutor<SetProjectRoleDefinitionsRequest, SetProjectRoleDefinitionsResponse> setRoleDefsExecutor;

    private final CommandExecutor<RebuildProjectPermissionsRequest, RebuildProjectPermissionsResponse> rebuildPermissionsExecutor;

    public SetFormRegionAccessRestrictionsHandler(CommandExecutor<GetProjectRoleDefinitionsRequest, GetProjectRoleDefinitionsResponse> getRoleDefsExecutor, CommandExecutor<SetProjectRoleDefinitionsRequest, SetProjectRoleDefinitionsResponse> setRoleDefsExecutor, CommandExecutor<RebuildProjectPermissionsRequest, RebuildProjectPermissionsResponse> rebuildPermissionsExecutor) {
        this.getRoleDefsExecutor = getRoleDefsExecutor;
        this.setRoleDefsExecutor = setRoleDefsExecutor;
        this.rebuildPermissionsExecutor = rebuildPermissionsExecutor;
    }


    @NotNull
    @Override
    public Resource getTargetResource(SetFormRegionAccessRestrictionsRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @NotNull
    @Override
    public Collection<Capability> getRequiredCapabilities() {
        return List.of(BasicCapability.valueOf("EditForms" ));
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetFormRegionAccessRestrictionsRequest.CHANNEL;
    }

    @Override
    public Class<SetFormRegionAccessRestrictionsRequest> getRequestClass() {
        return SetFormRegionAccessRestrictionsRequest.class;
    }

    @Override
    public Mono<SetFormRegionAccessRestrictionsResponse> handleRequest(SetFormRegionAccessRestrictionsRequest request, ExecutionContext executionContext) {

        logger.info("Setting form access restrictions: {}", request);
        var projectId = request.projectId();
        var accessRestrictions = request.accessRestrictions();

        var roleCapabilityMap = HashMultimap.<RoleId, FormRegionCapability>create();
        accessRestrictions.forEach(accessRestriction -> {
            var capabilityRoles = accessRestriction.capabilityRoles();
            capabilityRoles.keySet().forEach(id -> {
                var capability = FormRegionCapability.valueOf(id, accessRestriction.formRegionId());
                var roles = capabilityRoles.get(id);
                roles.forEach(role -> {
                    roleCapabilityMap.put(role, capability);
                });
            });
        });


        var response = getRoleDefsExecutor.execute(GetProjectRoleDefinitionsRequest.get(request.projectId()), executionContext)
                .thenCompose(roleDefsResponse -> {
                    var replacementRoleDefinitions = new ArrayList<RoleDefinition>();
                    var existingRoleDefinitions = roleDefsResponse.roleDefinitions();
                    existingRoleDefinitions.forEach(roleDef -> {
                        var capabilities = roleCapabilityMap.get(roleDef.roleId());
                        // Replace existing form region capabilities
                        var updatedRole = roleDef.withoutCapabilities(c -> c instanceof FormRegionCapability);
                        updatedRole = updatedRole.addCapabilities(capabilities);
                        replacementRoleDefinitions.add(updatedRole);
                    });
                    logger.info("Setting role definitions for project: {}", replacementRoleDefinitions);
                    return setRoleDefsExecutor.execute(SetProjectRoleDefinitionsRequest.get(projectId, replacementRoleDefinitions),
                                    executionContext)
                            .thenCompose(r2 -> {
                                return rebuildPermissionsExecutor.execute(RebuildProjectPermissionsRequest.get(projectId),
                                        executionContext)
                                        .thenApply(r -> new SetFormRegionAccessRestrictionsResponse(projectId));
                            });
                });
        return Mono.fromFuture(response);
    }
}
