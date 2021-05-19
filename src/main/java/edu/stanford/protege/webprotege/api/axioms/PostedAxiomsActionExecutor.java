package edu.stanford.protege.webprotege.api.axioms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.ProjectResource;
import edu.stanford.protege.webprotege.access.Subject;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.access.ActionId;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.stream.Stream;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class PostedAxiomsActionExecutor {

    @Nonnull
    private final AccessManager accessManager;

    @Nonnull
    private final ActionExecutor executor;

    @Inject
    public PostedAxiomsActionExecutor(@Nonnull AccessManager accessManager,
                                      @Nonnull ActionExecutor executor) {
        this.accessManager = accessManager;
        this.executor = executor;
    }

    public Response loadAxiomsAndExecuteAction(@Nonnull ProjectId projectId,
                                               @Nonnull UserId userId,
                                               @Nonnull ExecutionContext executionContext,
                                               @Nonnull UriInfo uriInfo,
                                               @Nonnull InputStream inputStream,
                                               @Nonnull String commitMessage,
                                               @Nonnull OWLDocumentFormat documentFormat,
                                               @Nonnull String mimeType,
                                               @Nonnull ActionFactory actionFactory,
                                               @Nonnull ActionId actionId) {
        if(!accessManager.hasPermission(Subject.forUser(userId),
                                        ProjectResource.forProject(projectId),
                                        actionId)) {
            return Response.status(FORBIDDEN)
                           .entity("You do not have permission to make changes to this project")
                           .build();
        }

        PostedAxiomsLoader axiomsLoader = new PostedAxiomsLoader(projectId,
                                                                 documentFormat,
                                                                 mimeType);
        PostedAxiomsLoadResponse loadResponse = axiomsLoader.loadAxioms(inputStream);
        if(loadResponse.isSuccess()) {
            Action<?> action = actionFactory.createAction(loadResponse.axioms(), commitMessage);
            Result result = executor.execute(action, executionContext);
            return Response.created(uriInfo.getAbsolutePath())
                           .entity(result)
                           .build();
        }
        else {
            return loadResponse.toResponse();
        }
    }

    public interface ActionFactory {

        Action<?> createAction(Stream<OWLAxiom> axioms, String commitMessage);
    }
}
