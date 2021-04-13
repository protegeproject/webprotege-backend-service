package edu.stanford.bmir.protege.web.shared.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
@JsonTypeName("GetChapSession")
public class GetChapSessionAction implements Action<GetChapSessionResult> {

    private UserId userId;

    /**
     * For serialization purposes only
     */
    private GetChapSessionAction() {
    }

    @JsonCreator
    public GetChapSessionAction(@JsonProperty("userId") UserId userId) {
        this.userId = checkNotNull(userId);
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetChapSessionAction)) {
            return false;
        }
        GetChapSessionAction other = (GetChapSessionAction) obj;
        return this.userId.equals(other.userId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetChapSessionAction")
                .addValue(userId)
                .toString();
    }
}
