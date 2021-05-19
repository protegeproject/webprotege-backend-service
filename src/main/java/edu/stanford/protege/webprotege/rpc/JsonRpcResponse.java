package edu.stanford.protege.webprotege.rpc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcResponse {

    public static JsonRpcResponse create(String id,
                                         JsonRpcError error) {
        return new AutoValue_JsonRpcResponse(null, error, id);
    }

    public static JsonRpcResponse create(String id,
                                         Result result) {
        return new AutoValue_JsonRpcResponse(result, null, id);
    }

    @JsonProperty("jsonrpc")
    public final String getJsonRpc() {
        return "2.0";
    }

    @JsonProperty("result")
    @Nullable
    public abstract Result getResultInternal();

    @JsonIgnore
    public Optional<Result> getResult() {
        return Optional.ofNullable(getResultInternal());
    }

    @JsonProperty("error")
    @Nullable
    public abstract JsonRpcError getErrorInternal();

    public Optional<JsonRpcError> getError() {
        return Optional.ofNullable(getErrorInternal());
    }

    @JsonProperty("id")
    public abstract String getId();
}
