package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.HashMultimap;
import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.*;
import edu.stanford.protege.webprotege.permissions.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

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
        return List.of(BasicCapability.valueOf("EditForms"));
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
        accessRestrictions.forEach(accessRestriction ->
                roleCapabilityMap.put(
                        accessRestriction.roleId(),
                        FormRegionCapability.valueOf(accessRestriction.capabilityId(), accessRestriction.formRegionId(), accessRestriction.criteria())
                )
        );

        var response = getRoleDefsExecutor.execute(GetProjectRoleDefinitionsRequest.get(request.projectId()), executionContext)
                .thenCompose(roleDefsResponse -> {
                    var replacementRoleDefinitions = new ArrayList<RoleDefinition>();
                    var existingRoleDefinitions = roleDefsResponse.roleDefinitions();
                    var regionIdsToUpdate = accessRestrictions.stream()
                            .map(FormRegionAccessRestriction::formRegionId)
                            .collect(Collectors.toSet());

                    existingRoleDefinitions.forEach(roleDef -> {
                        var updatedRole = roleDef.withoutCapabilities(c ->
                                c instanceof FormRegionCapability frc
                                        && regionIdsToUpdate.contains(frc.formRegionId())
                        );
                        updatedRole = updatedRole.addCapabilities(roleCapabilityMap.get(roleDef.roleId()));
                        replacementRoleDefinitions.add(updatedRole);
                    });
                    logger.info("Setting role definitions for project: {}", replacementRoleDefinitions);
                    return setRoleDefsExecutor.execute(SetProjectRoleDefinitionsRequest.get(projectId, replacementRoleDefinitions),
                                    executionContext)
                            .thenCompose(r2 -> rebuildPermissionsExecutor.execute(
                                            RebuildProjectPermissionsRequest.get(projectId),
                                            executionContext
                                    ).thenApply(r -> new SetFormRegionAccessRestrictionsResponse(projectId))
                            );
                });
        return Mono.fromFuture(response);
    }
}
