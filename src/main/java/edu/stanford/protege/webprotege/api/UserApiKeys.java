package edu.stanford.protege.webprotege.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
@Document(collection = "UserApiKeys")
@CompoundIndexes({
        @CompoundIndex(def = "apiKeys.apiKeyId", unique = true),
        @CompoundIndex(def = "apiKeys.apiKey", unique = true)
})
@Deprecated
public class UserApiKeys {

    public static final String USER_ID = "_id";

    public static final String API_KEYS = "apiKeys";

    public static final String API_KEYS__API_KEY_ID = API_KEYS + ".apiKeyId";

    public static final String API_KEYS__API_KEY = API_KEYS + ".apiKey";

    @Id
    private final UserId userId;

    private final List<ApiKeyRecord> apiKeys;

    public UserApiKeys(@Nonnull @JsonProperty("_id") UserId userId, @Nonnull List<ApiKeyRecord> apiKeys) {
        this.userId = checkNotNull(userId);
        this.apiKeys = ImmutableList.copyOf(apiKeys);
    }

    @JsonProperty("_id")
    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public List<ApiKeyRecord> getApiKeys() {
        return ImmutableList.copyOf(apiKeys);
    }
}
