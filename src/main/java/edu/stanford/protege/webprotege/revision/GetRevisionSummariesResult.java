package edu.stanford.protege.webprotege.revision;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetRevisionSummariesResult implements Result {

    private ImmutableList<RevisionSummary> revisionSummaries;

    /**
     * For serialization purposes only
     */
    private GetRevisionSummariesResult() {
    }

    private GetRevisionSummariesResult(ImmutableList<RevisionSummary> revisionSummaries) {
        this.revisionSummaries = checkNotNull(revisionSummaries);
    }

    public static GetRevisionSummariesResult create(ImmutableList<RevisionSummary> revisionSummaries) {
        return new GetRevisionSummariesResult(revisionSummaries);
    }

    public ImmutableList<RevisionSummary> getRevisionSummaries() {
        return revisionSummaries;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(revisionSummaries);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetRevisionSummariesResult)) {
            return false;
        }
        GetRevisionSummariesResult other = (GetRevisionSummariesResult) obj;
        return this.revisionSummaries.equals(other.revisionSummaries);
    }

    @Override
    public String toString() {
        return toStringHelper("GetRevisionSummariesResult")
                .addValue(revisionSummaries)
                .toString();
    }
}
