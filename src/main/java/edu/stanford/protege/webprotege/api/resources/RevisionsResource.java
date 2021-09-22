package edu.stanford.protege.webprotege.api.resources;



import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.actions.GetRevisionAction;
import edu.stanford.protege.webprotege.dispatch.actions.GetRevisionsAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionDetails;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 *
 * The changes within a context of a project
 */
@Deprecated
public class RevisionsResource {

    private final ProjectId projectId;

    private final ActionExecutor executor;

    @Inject

    public RevisionsResource(@Nonnull ProjectId projectId,
                             @Nonnull ActionExecutor executor) {
        this.projectId = checkNotNull(projectId);
        this.executor = checkNotNull(executor);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public ImmutableList<RevisionDetails> listChanges(@Context UserId userId,
                                                      @Context ExecutionContext executionContext,
                                                      @QueryParam("from")
                                                      @DefaultValue("1")
                                                              RevisionNumber from,
                                                      @QueryParam("to")
                                                      @DefaultValue("HEAD")
                                                              RevisionNumber to,
                                                      @QueryParam("userId")
                                                              UserId author) {
        GetRevisionsAction action = new GetRevisionsAction(projectId,
                                                           from,
                                                           to,
                                                           author);
        return executor.execute(action, new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.getUserId())).getRevisions();
    }

    @GET
    @Path("/{revisionNumber : [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRevision(@Context UserId userId,
                                @Context ExecutionContext executionContext,
                                @Context UriInfo uriInfo,
                                @PathParam("revisionNumber") RevisionNumber revisionNumber) {
            Optional<RevisionDetails> revisionDetails = executor.execute(new GetRevisionAction(projectId, revisionNumber), new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.getUserId()))
                                                                .getRevisionDetails();
            if(revisionDetails.isPresent()) {
                return Response.ok(revisionDetails.get())
                               .build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND)
                               .location(uriInfo.getAbsolutePath())
                               .build();
            }
    }
}
