package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetWatches")
public abstract class GetWatchesResult implements Result {

    @JsonCreator
    public static GetWatchesResult create(@JsonProperty("watches") Set<Watch> watches) {
        return new AutoValue_GetWatchesResult(watches);
    }

    public abstract Set<Watch> getWatches();
}
