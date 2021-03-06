package edu.stanford.protege.webprotege.api;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
@Deprecated
public class ApiKeyInfo {

    private final ApiKeyId apiKeyId;

    private final long createdAt;

    private final String purpose;

    public ApiKeyInfo(ApiKeyId apiKeyId,
                      long createdAt,
                      @Nonnull String purpose) {
        this.apiKeyId = checkNotNull(apiKeyId);
        this.createdAt = createdAt;
        this.purpose = checkNotNull(purpose);
    }

    public ApiKeyId getApiKeyId() {
        return apiKeyId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(createdAt, purpose);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApiKeyInfo)) {
            return false;
        }
        ApiKeyInfo other = (ApiKeyInfo) obj;
        return this.createdAt == other.createdAt
                && this.apiKeyId.equals(other.apiKeyId)
                && this.purpose.equals(other.purpose);
    }


    @Override
    public String toString() {
        return toStringHelper("ApiKeyInfo")
                .add("purpose", purpose)
                .add("createdAt", createdAt)
                .toString();
    }
}
