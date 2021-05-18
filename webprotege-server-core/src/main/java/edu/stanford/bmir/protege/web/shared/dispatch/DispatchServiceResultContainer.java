package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;




/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
@AutoValue

public abstract class DispatchServiceResultContainer {

    @JsonCreator
    public static DispatchServiceResultContainer create(@JsonProperty("result") Result result) {
        return new AutoValue_DispatchServiceResultContainer(result);
    }

    public abstract Result getResult();
}