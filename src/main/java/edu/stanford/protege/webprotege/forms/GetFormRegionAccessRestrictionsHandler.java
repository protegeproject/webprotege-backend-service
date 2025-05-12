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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                            var restrictions = getFormAccessRestrictions(response.roleDefinitions());
                            return new GetFormRegionAccessRestrictionsResponse(request.projectId(), restrictions);
                        })
        );
    }

    private static List<FormRegionAccessRestriction> getFormAccessRestrictions(List<RoleDefinition> roleDefinitions) {
       return roleDefinitions.stream()
                .flatMap(def -> {
                    return def.roleCapabilities()
                            .stream()
                            .filter(cap -> cap instanceof FormRegionCapability)
                            .map(cap -> (FormRegionCapability) cap)
                            .map(cap -> new FormRegionAccessRestriction(cap.formRegionId(), def.roleId(), cap.id(), cap.contextCriteria()));
                })
                .toList();

    }
}