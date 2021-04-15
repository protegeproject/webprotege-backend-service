package edu.stanford.bmir.protege.web.server.rpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-04-08
 */
@AutoValue
public abstract class JsonRpcParams {

    @JsonCreator
    public static JsonRpcParams create(@JsonProperty("action") Action action) {
        return new AutoValue_JsonRpcParams(action);
    }

    @JsonProperty("action")
    public abstract Action getAction();
}