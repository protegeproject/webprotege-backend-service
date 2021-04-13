package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DispatchServiceResultContainer implements IsSerializable {

    @JsonCreator
    public static DispatchServiceResultContainer create(@JsonProperty("result") Result result) {
        return new AutoValue_DispatchServiceResultContainer(result);
    }

    public abstract Result getResult();
}
