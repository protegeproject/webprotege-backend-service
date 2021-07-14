package edu.stanford.protege.webprotege.issues.events;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public class IssueLocked extends AbstractIssueEvent {

    public IssueLocked(@Nonnull UserId userId, long timestamp) {
        super(userId, timestamp);
    }


    private IssueLocked() {
    }

    @Override
    public int hashCode() {
        return Objects.hashCode("IssueLocked", getUserId(), getTimestamp());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueLocked)) {
            return false;
        }
        IssueLocked other = (IssueLocked) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueLocked")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .toString();
    }
}
