package edu.stanford.protege.webprotege.issues.events;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 16
 */
public class IssueSubscribed extends AbstractIssueEvent {

    @Nonnull
    private UserId subscriber;

    public IssueSubscribed(@Nonnull UserId userId,
                           long timestamp,
                           @Nonnull UserId subscriber) {
        super(userId, timestamp);
        this.subscriber = checkNotNull(subscriber);
    }


    private IssueSubscribed() {
    }

    @Nonnull
    public UserId getSubscriber() {
        return subscriber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId(), getTimestamp(), subscriber);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IssueSubscribed)) {
            return false;
        }
        IssueSubscribed other = (IssueSubscribed) obj;
        return this.getTimestamp() == other.getTimestamp()
                && this.getUserId().equals(other.getUserId())
                && this.getSubscriber().equals(other.getSubscriber());
    }


    @Override
    public String toString() {
        return toStringHelper("IssueSubscribed")
                .add("userId", getUserId())
                .add("timestamp", getTimestamp())
                .add("subscriber", subscriber)
                .toString();
    }
}
