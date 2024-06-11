package edu.stanford.protege.webprotege.mansyntax.render;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Objects;


public record EntityStatus(String status) implements Comparator<EntityStatus> {
    public EntityStatus(@Nonnull String status) {
        this.status = (String) Objects.requireNonNull(status);
    }

    @JsonCreator
    public static EntityStatus valueOf(String status) {
        return new EntityStatus(status);
    }

    @JsonValue
    public String status() {
        return this.status;
    }

    public int compare(EntityStatus o1, EntityStatus o2) {
        return o1.status.compareTo(o2.status);
    }
}
