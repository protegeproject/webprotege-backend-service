package edu.stanford.bmir.protege.web.server.api.rpc;

import edu.stanford.bmir.protege.web.server.api.ApiRootResource;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcError;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcRequest;
import edu.stanford.bmir.protege.web.server.rpc.JsonRpcResponse;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-13
 */
@Path("rpc")
public class RpcResource implements ApiRootResource {

    @Inject
    public RpcResource() {
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Path("/")
    public JsonRpcResponse handleRequest(@Context UserId userId,
                                         @Context UriInfo uriInfo,
                                         JsonRpcRequest request) {
        return JsonRpcResponse.create(request.getId(), JsonRpcError.create(3, "Not implemented"));
    }

}
