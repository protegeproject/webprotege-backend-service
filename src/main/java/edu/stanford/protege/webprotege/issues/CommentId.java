package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.stanford.protege.webprotege.common.ValueObject;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class CommentId implements ValueObject {

    private String id;

    @JsonCreator
    private CommentId(String id) {
        this.id = checkNotNull(id);
    }

    public static CommentId valueOf(@Nonnull String idString) {
        return new CommentId(checkNotNull(idString));
    }

    public static CommentId create() {
        //noinspection NonJREEmulationClassesInClientCode
        return new CommentId(UUID.randomUUID().toString());
    }


    private CommentId() {
    }

    @Override
    public String value() {
        return id;
    }

    @JsonValue
    @Nonnull
    public String getId() {
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
        if (!(obj instanceof CommentId)) {
            return false;
        }
        CommentId other = (CommentId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("CommentId")
                .addValue(id)
                .toString();
    }
}
