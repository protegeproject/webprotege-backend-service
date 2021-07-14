package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.stanford.protege.webprotege.ValueObject;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class ThreadId implements ValueObject {

    private String id;

    @JsonCreator
    public ThreadId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    public static ThreadId create() {
        //noinspection NonJREEmulationClassesInClientCode
        return new ThreadId(UUID.randomUUID().toString());
    }

    public static ThreadId valueOf(String id) {
        return new ThreadId(id);
    }


    private ThreadId() {
    }

    @JsonValue
    public String getId() {
        return id;
    }

    @Override
    public String value() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ThreadId)) {
            return false;
        }
        ThreadId other = (ThreadId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("ThreadId")
                .addValue(id)
                .toString();
    }
}

