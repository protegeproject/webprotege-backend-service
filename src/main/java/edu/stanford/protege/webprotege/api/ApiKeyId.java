package edu.stanford.protege.webprotege.api;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class ApiKeyId {

    private String id;

    private ApiKeyId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }


    private ApiKeyId() {
    }

    @Nonnull
    public static ApiKeyId valueOf(@Nonnull String id) {
        return new ApiKeyId(id);
    }

    @Nonnull
    public static ApiKeyId generateApiKeyId() {
        return valueOf(UUID.randomUUID().toString());
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
        if (!(obj instanceof ApiKeyId)) {
            return false;
        }
        ApiKeyId other = (ApiKeyId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("ApiKeyId")
                .addValue(id)
                .toString();
    }
}
