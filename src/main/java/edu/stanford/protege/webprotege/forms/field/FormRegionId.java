package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
public record FormRegionId(String value) implements ValueObject {

    public FormRegionId(String value) {
        this.value = checkFormat(value);
    }

    public static FormRegionId generate() {
        return get(UUID.randomUUID().toString());
    }

    @JsonCreator
    @Nonnull
    public static FormRegionId get(@Nonnull String id) {
        return new FormRegionId(id);
    }

    public static FormRegionId valueOf(String id) {
        return get(id);
    }

    private static String checkFormat(@Nonnull String id) {
        if (!UUIDUtil.isWellFormed(id)) {
            throw new IllegalArgumentException("Malformed FormRegionId: " + id);
        }
        return id;
    }

    @JsonValue
    @Override
    public String value() {
        return this.value;
    }
}
