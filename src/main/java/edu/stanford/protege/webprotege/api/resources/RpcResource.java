package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.api.ApiRootResource;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.rpc.JsonRpcRequest;
import edu.stanford.protege.webprotege.rpc.JsonRpcResponse;
import edu.stanford.protege.webprotege.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-13
 */
@Path("rpc")
public class RpcResource implements ApiRootResource {

    private static Logger logger = LoggerFactory.getLogger(RpcResource.class);

    @Nonnull
    private final ActionExecutor actionExecutor;

    @Inject
    public RpcResource(@Nonnull ActionExecutor actionExecutor) {
        this.actionExecutor = checkNotNull(actionExecutor);
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("/")
    public JsonRpcResponse handleRequest(@Context UserId userId,
                                         @Context UriInfo uriInfo,
                                         @Context ExecutionContext executionContext,
                                         JsonRpcRequest request) {
        var result = actionExecutor.execute(request.getParams().getAction(), executionContext);
        var response = JsonRpcResponse.create(request.getId(),
                               result);

        return response;
    }

}
