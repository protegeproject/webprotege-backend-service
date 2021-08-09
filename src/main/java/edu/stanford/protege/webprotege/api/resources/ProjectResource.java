package edu.stanford.protege.webprotege.api.resources;



import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.project.GetProjectDetailsAction;
import edu.stanford.protege.webprotege.project.ProjectDetails;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Apr 2018
 */
public class ProjectResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActionExecutor executor;

    @Nonnull
    private final AxiomsResourceFactory axiomsResourceFactory;

    @Nonnull
    private final DeleteAxiomsResourceFactory deleteAxiomsResourceFactory;

    @Nonnull
    private final RevisionsResourceFactory changesResourceFactory;

    @Nonnull
    private final FormsResourceFactory formsResourceFactory;

    @Nonnull
    private final ProjectSettingsResourceFactory projectSettingsResourceFactory;

    @SuppressWarnings("UnnecessaryFullyQualifiedName")

    @Inject
    public ProjectResource(@Nonnull ProjectId projectId,
                           @Nonnull ActionExecutor executor,
                           @Nonnull AxiomsResourceFactory axiomsResourceFactory,
                           @Nonnull DeleteAxiomsResourceFactory deleteAxiomsResourceFactory,
                           @Nonnull RevisionsResourceFactory changesResourceFactory,
                           @Nonnull FormsResourceFactory formsResourceFactory,
                           @Nonnull ProjectSettingsResourceFactory projectSettingsResourceFactory) {
        this.projectId = checkNotNull(projectId);
        this.executor = checkNotNull(executor);
        this.axiomsResourceFactory = checkNotNull(axiomsResourceFactory);
        this.deleteAxiomsResourceFactory = checkNotNull(deleteAxiomsResourceFactory);
        this.changesResourceFactory = checkNotNull(changesResourceFactory);
        this.formsResourceFactory = checkNotNull(formsResourceFactory);
        this.projectSettingsResourceFactory = checkNotNull(projectSettingsResourceFactory);
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/")
    public Response getProjectDetails(@Context UserId userId, @Context UriInfo uriInfo, @Context ExecutionContext executionContext) {
        ProjectDetails projectDetails = executor.execute(new GetProjectDetailsAction(projectId), executionContext)
                                                .getProjectDetails();
        return Response.ok(projectDetails).build();
    }

    @Path("revisions")
    public RevisionsResource getRevisions() {
        return changesResourceFactory.create(projectId);
    }

    @Path("axioms")
    public AxiomsResource getProjectAxiomsResource() {
        return axiomsResourceFactory.create(projectId);
    }

    @Path("delete-axioms")
    public DeleteAxiomsResource getProjectDeleteAxiomsResource() {
        return deleteAxiomsResourceFactory.create(projectId);
    }

    @Path("forms")
    public FormsResource getForms() {
        return formsResourceFactory.create(projectId);
    }

    @Path("settings")
    public ProjectSettingsResource getSettings() {
        return projectSettingsResourceFactory.create(projectId);
    }
}
