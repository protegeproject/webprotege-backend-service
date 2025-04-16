package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.*;
import edu.stanford.protege.webprotege.authorization.GetProjectRoleDefinitionsRequest;
import edu.stanford.protege.webprotege.authorization.GetProjectRoleDefinitionsResponse;
import edu.stanford.protege.webprotege.authorization.RoleDefinition;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.HashMultimap.create;

@WebProtegeHandler
public class GetFormRegionAccessRestrictionsHandler implements CommandHandler<GetFormRegionAccessRestrictionsRequest, GetFormRegionAccessRestrictionsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetFormRegionAccessRestrictionsRequest.class);

    private final CommandExecutor<GetProjectRoleDefinitionsRequest, GetProjectRoleDefinitionsResponse> getRoleDefsExecutor;

    public GetFormRegionAccessRestrictionsHandler(CommandExecutor<GetProjectRoleDefinitionsRequest, GetProjectRoleDefinitionsResponse> getRoleDefsExecutor) {
        this.getRoleDefsExecutor = getRoleDefsExecutor;
    }

    @Override
    public @NotNull String getChannelName() {
        return GetFormRegionAccessRestrictionsRequest.CHANNEL;
    }

    @Override
    public Class<GetFormRegionAccessRestrictionsRequest> getRequestClass() {
        return GetFormRegionAccessRestrictionsRequest.class;
    }

    @Override
    public Mono<GetFormRegionAccessRestrictionsResponse> handleRequest(GetFormRegionAccessRestrictionsRequest request,
                                                                       ExecutionContext executionContext) {
        var roleDefsRequest = GetProjectRoleDefinitionsRequest.get(request.projectId());

        return Mono.fromFuture(
                getRoleDefsExecutor.execute(roleDefsRequest, executionContext)
                        .thenApply(response -> {
                            var restrictions = computeFormRegionAccessRestrictions(response.roleDefinitions());
                            return new GetFormRegionAccessRestrictionsResponse(request.projectId(), restrictions);
                        })
        );
    }

    private static @NotNull List<FormRegionAccessRestrictions> computeFormRegionAccessRestrictions(List<RoleDefinition> roleDefinitions) {
        var region2Roles = computeRegionToRolesMap(roleDefinitions);
        var restrictionsList = new ArrayList<FormRegionAccessRestrictions>();
        for (var formRegionId : region2Roles.rowKeySet()) {
            for(var capabilityId : region2Roles.columnKeySet()) {
                var roleIds = region2Roles.get(formRegionId, capabilityId);
                var multimap = HashMultimap.<String, RoleId>create();
                multimap.putAll(capabilityId, roleIds);
                restrictionsList.add(new FormRegionAccessRestrictions(formRegionId, multimap));
            }
        }
        return restrictionsList;
    }

    private static @NotNull Table<FormRegionId, String, List<RoleId>> computeRegionToRolesMap(List<RoleDefinition> roleDefinitions) {
        Table<FormRegionId, String, List<RoleId>> result = HashBasedTable.create();
        logger.info("Computing region access restrictions");
        var rolesWithWriteAccess = HashMultimap.<FormRegionId, RoleId>create();
        for (var roleDefinition : roleDefinitions) {
            logger.info("    Processing role definition {}", roleDefinition);
            var roleId = roleDefinition.roleId();
            roleDefinition.roleCapabilities().stream()
                    .filter(cap -> cap instanceof FormRegionCapability)
                    .map(cap -> (FormRegionCapability) cap)
                    .forEach(formRegionCapability -> {
                        var roles = result.get(formRegionCapability.formRegionId(), formRegionCapability.id());
                        if(roles == null) {
                            roles = new ArrayList<RoleId>();
                            result.put(formRegionCapability.formRegionId(), formRegionCapability.id(), roles);
                        }
                        roles.add(roleId);
                    });
        }
        logger.info("    Computed: {}", rolesWithWriteAccess);
        return result;
    }
}