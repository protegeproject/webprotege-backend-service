package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.UUIDUtil;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
@AutoValue

public abstract class FormGroupId {

    @JsonCreator
    public static FormGroupId get(@Nonnull String id) {
        checkFormat(id);
        return new AutoValue_FormGroupId(id);
    }

    public static FormGroupId generate() {
        return get(UUID.randomUUID().toString());
    }

    public static void checkFormat(@Nonnull String id) {
        if (!UUIDUtil.isWellFormed(id)) {
            throw new RuntimeException("Malformed FormGroupId.  FormGroupIds should be UUIDs");
        }
    }

    @JsonValue
    @Nonnull
    public abstract String getId();
}
