package edu.stanford.protege.webprotege.access;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static edu.stanford.protege.webprotege.access.RoleAssignment.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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

    private final CommandExecutor<GetAuthorizedActionsRequest, GetAuthorizedActionsResponse> getAuthorizedActionsExecutor;

    private final CommandExecutor<GetAuthorizationStatusRequest, GetAuthorizationStatusResponse> getAuthorizationStatusExecutor;

    private final CommandExecutor<GetAuthorizedSubjectsRequest, GetAuthorizedSubjectsResponse> getAuthorizedSubjectsExecutor;

    private final CommandExecutor<GetAuthorizedResourcesRequest, GetAuthorizedResourcesResponse> getAuthorizedResourcesExecutor;

    public AccessManagerImpl(CommandExecutor<GetAssignedRolesRequest, GetAssignedRolesResponse> getAssignedRolesExecutor,
                             CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor,
                             CommandExecutor<GetRolesRequest, GetRolesResponse> getRolesRequestExecutor,
                             CommandExecutor<GetAuthorizedActionsRequest, GetAuthorizedActionsResponse> getAuthorizedActionsExecutor,
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

    @NotNull
    @Override
    public Collection<RoleId> getAssignedRoles(@NotNull Subject subject, @NotNull Resource resource) {

        try {
            return getAssignedRolesExecutor.execute(new GetAssignedRolesRequest(subject,
                                                                                resource),
                                                    new ExecutionContext())
                    .get()
                    .roles();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error while waiting for assigned roles", e);
            return Collections.emptySet();
        }
    }

    @Override
    public void setAssignedRoles(@NotNull Subject subject,
                                 @NotNull Resource resource,
                                 @NotNull Collection<RoleId> roleIds) {
        try {
            var response = setAssignedRolesExecutor.execute(new SetAssignedRolesRequest(subject, resource, Set.copyOf(roleIds)),
                                                            new ExecutionContext());
            response.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error when setting assigned roles", e);
        }
    }

    @NotNull
    @Override
    public Collection<RoleId> getRoleClosure(@NotNull Subject subject, @NotNull Resource resource) {
        try {
            return getRolesRequestExecutor.execute(new GetRolesRequest(subject, resource),
                                                   new ExecutionContext())
                    .get()
                    .roles();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error when getting roles", e);
            return Collections.emptySet();
        }
    }

    @NotNull
    @Override
    public Set<ActionId> getActionClosure(@NotNull Subject subject, @NotNull Resource resource) {
        try {
            return getAuthorizedActionsExecutor.execute(new GetAuthorizedActionsRequest(resource, subject),
                                                        new ExecutionContext())
                    .get()
                    .actionIds();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error when getting authorized actions", e);
            return Collections.emptySet();
        }
    }

    @Override
    public boolean hasPermission(@NotNull Subject subject, @NotNull Resource resource, @NotNull ActionId actionId) {
        try {
            return getAuthorizationStatusExecutor.execute(new GetAuthorizationStatusRequest(resource, subject, actionId),
                                                          new ExecutionContext())
                    .get()
                    .authorizationStatus().equals(AuthorizationStatus.AUTHORIZED);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error when getting authorization status", e);
            return false;
        }
    }

    @Override
    public boolean hasPermission(@NotNull Subject subject,
                                 @NotNull Resource resource,
                                 @NotNull BuiltInAction builtInAction) {
        return hasPermission(subject, resource, builtInAction.getActionId());
    }


    @Override
    public Collection<Subject> getSubjectsWithAccessToResource(Resource resource, BuiltInAction action) {
        try {
            return getAuthorizedSubjectsExecutor.execute(new GetAuthorizedSubjectsRequest(resource,
                                                                                          action.getActionId()),
                                                         new ExecutionContext())
                    .get()
                                                .subjects();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error when getting authorized subjects", e);
            return Collections.emptySet();
        }
    }

    @Override
    public Collection<Subject> getSubjectsWithAccessToResource(Resource resource) {
        return null;
    }

    @Override
    public Collection<Resource> getResourcesAccessibleToSubject(Subject subject, ActionId actionId) {
        try {
            return getAuthorizedResourcesExecutor.execute(new GetAuthorizedResourcesRequest(subject, actionId),
                                                          new ExecutionContext())
                    .get()
                    .resources();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error getting authorized resources", e);
            return Collections.emptySet();
        }
    }

    @Override
    public void rebuild() {

    }
}
