package edu.stanford.protege.webprotege.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.util.UUIDUtil;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
@AutoValue

public abstract class EntitySearchFilterId {

    /**
     * Gets a {@link EntitySearchFilter}
     * @param id The search filter id.  This must be a UUID in the standard UUID format.
     */
    @JsonCreator
    @Nonnull
    public static EntitySearchFilterId get(@Nonnull String id) {
        if(!UUIDUtil.isWellFormed(checkNotNull(id))) {
            throw new IllegalArgumentException("Malformed filter id: " + id);
        }
        return new AutoValue_EntitySearchFilterId(id);
    }

    /**
     * Create a new filter id using a random UUID.  This method only works
     * on the webprotege.
     */
    @Nonnull
    public static EntitySearchFilterId createFilterId() {
        return get(UUID.randomUUID().toString());
    }

    @JsonValue
    @Nonnull
    public abstract String getId();
}
