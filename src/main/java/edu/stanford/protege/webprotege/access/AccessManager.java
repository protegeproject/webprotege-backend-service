package edu.stanford.protege.webprotege.access;


import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Jan 2017
 */
public interface AccessManager {

    /**
     * Get the role ids that have been assigned to the specified subject for the specified resource.
     * @param subject The subject.
     * @param resource The resoruce.
     * @return The assigned role ids for the subject and resource pair.
     */
    @Nonnull
    Collection<RoleId> getAssignedRoles(@Nonnull Subject subject,
                                        @Nonnull Resource resource);

    /**
     * Sets the assigned roles for the specified subject and resource pair.
     * @param subject The subject.
     * @param resource The resource.
     * @param roleIds The role ids to be assigned.  These will replace any existing assigned role ids for the
     *                specified subject and resource pair.
     */
    void setAssignedRoles(@Nonnull Subject subject,
                          @Nonnull Resource resource,
                          @Nonnull Collection<RoleId> roleIds);

    CompletableFuture<SetAssignedRolesResponse> setAssignedRolesAsync(@Nonnull Subject subject,
                                                  @Nonnull Resource resource,
                                                  @Nonnull Collection<RoleId> roleIds);

    /**
     * Gets the role closure for the specified subject and resource pair.
     * @param subject The subject.
     * @param resource The resource.
     * @return A collection of role ids that are in the role closure for the specified subject and resource pair.
     */
    @Nonnull
    Collection<RoleId> getRoleClosure(@Nonnull Subject subject,
                                      @Nonnull Resource resource);

    /**
     * Gets the action closure for the specified subject and resource pair.
     * @param subject The subject.
     * @param resource The resource.
     * @return A collection of action ids that belong to the role closure of the specified subject and resource pair.
     */
    @Nonnull
    Set<Capability> getCapabilityClosure(@Nonnull Subject subject,
                                         @Nonnull Resource resource,
                                         ExecutionContext executionContext);

    /**
     * Tests to see if the specified subject has permission to execute the specified action on the specified resource.
     * @param subject The subject.
     * @param resource The resource on which the action should be executed.
     * @param capability The action to be executed.
     * @return {@code true} if the subject has permission to execute the specified action on the specified resource,
     * otherwise {@code false}.
     */
    @Deprecated
    boolean hasPermission(@Nonnull Subject subject,
                          @Nonnull Resource resource,
                          @Nonnull Capability capability);

    @Deprecated
    boolean hasPermission(@Nonnull Subject subject,
                          @Nonnull ApplicationResource resource,
                          @Nonnull Capability capability,
                          ExecutionContext executionContext);

    /**
     * Tests to see if the specified subject has permission to execute the specified action on the specified resource.
     * @param subject The subject.
     * @param resource The resource on which the action should be executed.
     * @param builtInCapability The action to be executed.
     * @return {@code true} if the subject has permission to execute the specified action on the specified resource,
     * otherwise {@code false}.
     */
    boolean hasPermission(@Nonnull Subject subject,
                          @Nonnull Resource resource,
                          @Nonnull BuiltInCapability builtInCapability);

    boolean hasPermission(ExecutionContext executionContext,
                          @Nonnull Subject subject,
                          @Nonnull Resource resource,
                          @Nonnull BuiltInCapability builtInCapability);

    Collection<Subject> getSubjectsWithAccessToResource(Resource resource);

    Collection<Subject> getSubjectsWithAccessToResource(Resource resource, BuiltInCapability action);

    Collection<Resource> getResourcesAccessibleToSubject(Subject subject, Capability capability, ExecutionContext executionContext);


    /**
     * Rebuilds the role and action closure for all subjects and resources.
     */
    void rebuild();
}