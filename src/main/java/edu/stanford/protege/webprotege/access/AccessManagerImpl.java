package edu.stanford.protege.webprotege.access;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import edu.stanford.protege.webprotege.authorization.api.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
public class AccessManagerImpl implements AccessManager {

    private static final Logger logger = LoggerFactory.getLogger(AccessManagerImpl.class);

    private CommandExecutor<GetAssignedRolesRequest, GetAssignedRolesResponse> getAssignedRolesExecutor;

    private CommandExecutor<SetAssignedRolesRequest, SetAssignedRolesResponse> setAssignedRolesExecutor;

    @NotNull
    @Override
    public Collection<RoleId> getAssignedRoles(@NotNull Subject subject, @NotNull Resource resource) {
        try {
            return getAssignedRolesExecutor.execute(new GetAssignedRolesRequest(subject,
                                                                                resource))
                    .get()
                    .roles();
        } catch (InterruptedException | ExecutionException | IOException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public void setAssignedRoles(@NotNull Subject subject,
                                 @NotNull Resource resource,
                                 @NotNull Collection<RoleId> roleIds) {
        try {
            setAssignedRolesExecutor.execute(new SetAssignedRolesRequest(subject, resource, Set.copyOf(roleIds)));
        } catch (IOException | ExecutionException | InterruptedException e) {
            logger.error("Error when setting assigned roles", e);
        }
    }

    @NotNull
    @Override
    public Collection<RoleId> getRoleClosure(@NotNull Subject subject, @NotNull Resource resource) {
        return null;
    }

    @NotNull
    @Override
    public Set<ActionId> getActionClosure(@NotNull Subject subject, @NotNull Resource resource) {
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull Subject subject, @NotNull Resource resource, @NotNull ActionId actionId) {
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull Subject subject,
                                 @NotNull Resource resource,
                                 @NotNull BuiltInAction builtInAction) {
        return false;
    }

    @Override
    public Collection<Subject> getSubjectsWithAccessToResource(Resource resource) {
        return null;
    }

    @Override
    public Collection<Subject> getSubjectsWithAccessToResource(Resource resource, BuiltInAction action) {
        return null;
    }

    @Override
    public Collection<Resource> getResourcesAccessibleToSubject(Subject subject, ActionId actionId) {
        return null;
    }

    @Override
    public void rebuild() {

    }
}
