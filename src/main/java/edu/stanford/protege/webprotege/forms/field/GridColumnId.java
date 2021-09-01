package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.util.UUIDUtil;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */

@AutoValue
@JsonTypeName("GridColumnId")
public abstract class GridColumnId implements FormRegionId {

    @JsonCreator
    @Nonnull
    public static GridColumnId get(@JsonProperty("id") @Nonnull String id) {
        checkFormat(id);
        return new AutoValue_GridColumnId(id);
    }

    @Override
    public String value() {
        return getId();
    }

    public static GridColumnId valueOf(String id) {
        return get(id);
    }

    private static void checkFormat(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(id)) {
            throw new IllegalArgumentException("Malformed GridColumnId: " + id);
        }
    }

    @Override
    public abstract String getId();
}
