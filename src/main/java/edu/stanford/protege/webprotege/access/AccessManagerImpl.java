package edu.stanford.protege.webprotege.access;


import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jan 2017
 */
@Component
public class AccessManagerImpl implements AccessManager {

    private static final Logger logger = LoggerFactory.getLogger(AccessManagerImpl.class);

    private final CommandExecutor<GetAssignedRolesRequest, GetAssignedRolesResponse> getAssignedRolesExecutor;

    private final CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor;

    private final CommandExecutor<GetRolesRequest, GetRolesResponse> getRolesRequestExecutor;

    private final CommandExecutor<GetAuthorizedCapabilitiesRequest, GetAuthorizedCapabilitiesResponse> getAuthorizedActionsExecutor;

    private final CommandExecutor<GetAuthorizationStatusRequest, GetAuthorizationStatusResponse> getAuthorizationStatusExecutor;

    private final CommandExecutor<GetAuthorizedSubjectsRequest, GetAuthorizedSubjectsResponse> getAuthorizedSubjectsExecutor;

    private final CommandExecutor<GetAuthorizedResourcesRequest, GetAuthorizedResourcesResponse> getAuthorizedResourcesExecutor;

    public AccessManagerImpl(CommandExecutor<GetAssignedRolesRequest, GetAssignedRolesResponse> getAssignedRolesExecutor,
                             CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor,
                             CommandExecutor<GetRolesRequest, GetRolesResponse> getRolesRequestExecutor,
                             CommandExecutor<GetAuthorizedCapabilitiesRequest, GetAuthorizedCapabilitiesResponse> getAuthorizedActionsExecutor,
                             CommandExecutor<GetAuthorizationStatusRequest, GetAuthorizationStatusResponse> getAuthorizationStatusExecutor,
                             CommandExecutor<GetAuthorizedSubjectsRequest, GetAuthorizedSubjectsResponse> getAuthorizedSubjectsExecutor,
                             CommandExecutor<GetAuthorizedResourcesRequest, GetAuthorizedResourcesResponse> getAuthorizedResourcesExecutor) {
        this.getAssignedRolesExecutor = getAssignedRolesExecutor;
        this.setAssignedRolesExecutor = setAssignedRolesExecutor;
        this.getRolesRequestExecutor = getRolesRequestExecutor;
        this.getAuthorizedActionsExecutor = getAuthorizedActionsExecutor;
        this.getAuthorizationStatusExecutor = getAuthorizationStatusExecutor;
        this.getAuthorizedSubjectsExecutor = getAuthorizedSubjectsExecutor;
        this.getAuthorizedResourcesExecutor = getAuthorizedResourcesExecutor;
    }

    @Nonnull
    @Override
    public Collection<RoleId> getAssignedRoles(@Nonnull Subject subject, @Nonnull Resource resource) {

        try {
            return getAssignedRolesExecutor.execute(new GetAssignedRolesRequest(subject,
                                                                                resource),
                                                    new ExecutionContext())
                    .get(5, TimeUnit.SECONDS)
                    .roles();
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error while waiting for assigned roles", e);
            return Collections.emptySet();
        }
    }

    @Override
    public void setAssignedRoles(@Nonnull Subject subject,
                                 @Nonnull Resource resource,
                                 @Nonnull Collection<RoleId> roleIds) {
        try {
            var response = setAssignedRolesExecutor.execute(new SetAssignedRolesRequest(subject, resource, Set.copyOf(roleIds)),
                                                            new ExecutionContext());
            response
                    .get(5, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error when setting assigned roles", e);
        }
    }

    @Override
    public CompletableFuture<SetAssignedRolesResponse> setAssignedRolesAsync(@Nonnull Subject subject,
                                                         @Nonnull Resource resource,
                                                         @Nonnull Collection<RoleId> roleIds) {
        return setAssignedRolesExecutor.execute(new SetAssignedRolesRequest(subject, resource, Set.copyOf(roleIds)),
                                                new ExecutionContext());
    }

    @Nonnull
    @Override
    public Collection<RoleId> getRoleClosure(@Nonnull Subject subject, @Nonnull Resource resource) {
        try {
            return getRolesRequestExecutor.execute(new GetRolesRequest(subject, resource),
                                                   new ExecutionContext())
                    .get(5, TimeUnit.SECONDS)
                    .roles();
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error when getting roles", e);
            return Collections.emptySet();
        }
    }

    @Nonnull
    @Override
    public Set<Capability> getCapabilityClosure(@Nonnull Subject subject, @Nonnull Resource resource, ExecutionContext executionContext) {
        try {
            return getAuthorizedActionsExecutor.execute(new GetAuthorizedCapabilitiesRequest(resource, subject),
                                                        executionContext)
                    .get(5, TimeUnit.SECONDS)
                    .capabilities();
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error when getting authorized actions", e);
            return Collections.emptySet();
        }
    }

    @Override
    public boolean hasPermission(@Nonnull Subject subject, @Nonnull Resource resource, @Nonnull Capability capability) {
        try {
            GetAuthorizationStatusResponse response = getAuthorizationStatusExecutor.execute(new GetAuthorizationStatusRequest(resource, subject, capability),
                                                          new ExecutionContext())
                    .get(5, TimeUnit.SECONDS);
            return response.authorizationStatus().equals(AuthorizationStatus.AUTHORIZED);

        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error when getting authorization status", e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(@Nonnull Subject subject, @Nonnull ApplicationResource resource, @Nonnull Capability actionId, ExecutionContext executionContext) {
        try {
            return getAuthorizationStatusExecutor.execute(new GetAuthorizationStatusRequest(resource, subject, actionId),
                            executionContext)
                    .get(5, TimeUnit.SECONDS)
                    .authorizationStatus().equals(AuthorizationStatus.AUTHORIZED);
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error when getting authorization status", e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(@Nonnull Subject subject,
                                 @Nonnull Resource resource,
                                 @Nonnull BuiltInCapability builtInCapability) {
        return hasPermission(subject, resource, builtInCapability.getCapability());
    }


    @Override
    public Collection<Subject> getSubjectsWithAccessToResource(Resource resource, BuiltInCapability action) {
        try {
            return getAuthorizedSubjectsExecutor.execute(new GetAuthorizedSubjectsRequest(resource,
                                                                                          action.getCapability()),
                                                         new ExecutionContext())
                    .get(5, TimeUnit.SECONDS)
                                                .subjects();
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error when getting authorized subjects", e);
            return Collections.emptySet();
        }
    }

    @Override
    public Collection<Subject> getSubjectsWithAccessToResource(Resource resource) {
        return getSubjectsWithAccessToResource(resource, BuiltInCapability.VIEW_PROJECT);
    }

    @Override
    public Collection<Resource> getResourcesAccessibleToSubject(Subject subject, Capability actionId, ExecutionContext executionContext) {
        try {
            return getAuthorizedResourcesExecutor.execute(new GetAuthorizedResourcesRequest(subject, actionId),
                            executionContext)
                    .get(5, TimeUnit.SECONDS)
                    .resources();
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            logger.error("Error getting authorized resources", e);
            return Collections.emptySet();
        }
    }

    @Override
    public void rebuild() {

    }
}
