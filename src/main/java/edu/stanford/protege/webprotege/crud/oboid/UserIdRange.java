package edu.stanford.protege.webprotege.crud.oboid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
@AutoValue

public abstract class UserIdRange implements Serializable {

    public static long getDefaultEnd() {
        return Long.MAX_VALUE;
    }

    public static long getDefaultStart() {
        return 0;
    }

    @Nonnull
    @JsonCreator
    public static UserIdRange get(@JsonProperty("userId") @Nonnull UserId userId,
                                  @JsonProperty("start") long start,
                                  @JsonProperty("end") long end) {
        return new AutoValue_UserIdRange(userId, start, end);
    }

    @Nonnull
    public abstract UserId getUserId();

    public abstract long getStart();

    public abstract long getEnd();
}
