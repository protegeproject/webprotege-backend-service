package edu.stanford.protege.webprotege.sharing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.ValueObject;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 *
 * Identifies a Person in a sharing setting.  The person may or may not be a user in webprotege.
 */
@AutoValue

public abstract class PersonId implements Serializable, Comparable<PersonId>, ValueObject {

    @JsonCreator
    @Nonnull
    public static PersonId get(@Nonnull String id) {
        return new AutoValue_PersonId(id);
    }

    public static PersonId valueOf(String id) {
        return get(id);
    }

    @Nonnull
    public static PersonId of(UserId userId) {
        return get(userId.getUserName());
    }

    @JsonValue
    @Nonnull
    public abstract String getId();

    @Override
    public String value() {
        return getId();
    }

    @Override
    public int compareTo(@Nonnull PersonId o) {
        if(this == o) {
            return 0;
        }
        int diff = this.getId().compareToIgnoreCase(o.getId());
        if(diff != 0) {
            return diff;
        }
        return this.getId().compareTo(o.getId());
    }
}
