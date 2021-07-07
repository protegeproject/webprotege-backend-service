package edu.stanford.protege.webprotege.api;

import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.rpc.JsonRpcRequest;
import edu.stanford.protege.webprotege.rpc.JsonRpcResponse;
import edu.stanford.protege.webprotege.session.WebProtegeSessionImpl;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-07
 */
@RestController
public class RpcController {

    private ActionExecutor actionExecutor;

    public RpcController(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    public JsonRpcResponse handleRpc(@RequestBody JsonRpcRequest request,
                                     @Context HttpSession session) {
        var result = actionExecutor.execute(request.getParams().getAction(),
                               new ExecutionContext(new WebProtegeSessionImpl(session)));
        return JsonRpcResponse.create(request.getId(),
                                      result);
    }


}
