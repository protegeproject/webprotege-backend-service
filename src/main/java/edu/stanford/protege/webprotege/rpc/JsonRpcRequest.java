package edu.stanford.protege.webprotege.rpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Action;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcRequest {


    @JsonCreator
    public static JsonRpcRequest create(@JsonProperty("params") JsonRpcParams params,
                                        @JsonProperty("id") String id) {
        return new AutoValue_JsonRpcRequest(params, id);
    }

    public static JsonRpcRequest create(Action action) {
        return create(JsonRpcParams.create(action), UUID.randomUUID().toString());
    }

    @JsonProperty("jsonrpc")
    public final String getJsonRpc() {
        return "2.0";
    }

    @JsonProperty("method")
    public String getMethod() {
        return "executeAction";
    }

    @JsonProperty("params")
    public abstract JsonRpcParams getParams();

    @JsonProperty("id")
    public abstract String getId();
}
