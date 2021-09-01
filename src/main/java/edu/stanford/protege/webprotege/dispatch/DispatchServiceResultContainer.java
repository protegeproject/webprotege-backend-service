package edu.stanford.protege.webprotege.dispatch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.Response;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/03/2013
 */
@AutoValue
public abstract class DispatchServiceResultContainer {

    @JsonCreator
    public static DispatchServiceResultContainer create(@JsonProperty("response") Response result) {
        return new AutoValue_DispatchServiceResultContainer(result);
    }

    public abstract Response getResult();
}
