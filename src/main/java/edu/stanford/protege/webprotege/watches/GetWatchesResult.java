package edu.stanford.protege.webprotege.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@AutoValue

@JsonTypeName("GetWatches")
public abstract class GetWatchesResult implements Result {

    @JsonCreator
    public static GetWatchesResult create(@JsonProperty("watches") Set<Watch> watches) {
        return new AutoValue_GetWatchesResult(watches);
    }

    public abstract Set<Watch> getWatches();
}
