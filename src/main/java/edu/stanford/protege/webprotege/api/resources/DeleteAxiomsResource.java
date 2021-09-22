package edu.stanford.protege.webprotege.api.resources;



import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.api.axioms.PostedAxiomsActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RioTurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;

import static edu.stanford.protege.webprotege.download.DownloadFormat.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
@Deprecated
public class DeleteAxiomsResource {

    private static final String DELETED_AXIOMS = "Deleted external axioms";

    @Nonnull
    private final PostedAxiomsActionExecutor postedAxiomsActionExecutor;

    @Nonnull
    private final ProjectId projectId;


    public DeleteAxiomsResource(@Nonnull PostedAxiomsActionExecutor postedAxiomsActionExecutor,
                                @Nonnull ProjectId projectId) {
        this.postedAxiomsActionExecutor = postedAxiomsActionExecutor;
        this.projectId = projectId;
    }

    @POST
    @Consumes("text/owl-functional")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDeleteAxiomsInFunctionalSyntax(@Context UserId userId,
                                                         @Context ExecutionContext executionContext,
                                                      @Context UriInfo uriInfo,
                                                      InputStream inputStream,
                                                      @QueryParam("msg") @DefaultValue(DELETED_AXIOMS) String msg) {
        return loadAndDeleteAxioms(userId, executionContext,
                                   uriInfo,
                                   inputStream,
                                   msg,
                                   new FunctionalSyntaxDocumentFormat(),
                                   FUNCTIONAL_SYNTAX.getMimeType());
    }

    @POST
    @Consumes("application/rdf+xml")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDeleteAxiomsInRdfXml(@Context UserId userId,
                                               @Context ExecutionContext executionContext,
                                            @Context UriInfo uriInfo,
                                            InputStream inputStream,
                                            @QueryParam("msg") @DefaultValue(DELETED_AXIOMS) String msg) {
        return loadAndDeleteAxioms(userId, executionContext,
                                   uriInfo,
                                   inputStream,
                                   msg,
                                   new RDFXMLDocumentFormat(),
                                   RDF_XML.getMimeType());
    }
    
    @POST
    @Consumes("text/ttl")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDeleteAxiomsInTurtle(@Context UserId userId,
                                            @Context UriInfo uriInfo,
                                               @Context ExecutionContext executionContext,
                                            InputStream inputStream,
                                            @QueryParam("msg") @DefaultValue(DELETED_AXIOMS) String msg) {
        return loadAndDeleteAxioms(userId, executionContext,
                                   uriInfo,
                                   inputStream,
                                   msg,
                                   new RioTurtleDocumentFormat(),
                                   RDF_TURLE.getMimeType());
    }


    private Response loadAndDeleteAxioms(@Nonnull UserId userId,
                                         @Nonnull ExecutionContext executionContext, @Nonnull UriInfo uriInfo,
                                         @Nonnull InputStream inputStream,
                                         @Nonnull String commitMessage,
                                         @Nonnull OWLDocumentFormat documentFormat,
                                         @Nonnull String mimeType) {
        return loadAxiomsAndExecuteAction(userId, executionContext, uriInfo,
                                          inputStream,
                                          commitMessage,
                                          documentFormat,
                                          mimeType,
                                          (axioms, msg) -> new DeleteAxiomsAction(projectId, axioms, msg));
    }

    private Response loadAxiomsAndExecuteAction(@Nonnull UserId userId,
                                                @Nonnull ExecutionContext executionContext,
                                                @Nonnull UriInfo uriInfo,
                                                @Nonnull InputStream inputStream,
                                                @Nonnull String commitMessage,
                                                @Nonnull OWLDocumentFormat documentFormat,
                                                @Nonnull String mimeType,
                                                PostedAxiomsActionExecutor.ActionFactory actionFactory) {
        return postedAxiomsActionExecutor.loadAxiomsAndExecuteAction(projectId,
                                                              userId,
                                                              executionContext,
                                                              uriInfo,
                                                              inputStream,
                                                              commitMessage,
                                                              documentFormat,
                                                              mimeType,
                                                              actionFactory,
                                                              BuiltInAction.EDIT_ONTOLOGY.getActionId());
    }

}
