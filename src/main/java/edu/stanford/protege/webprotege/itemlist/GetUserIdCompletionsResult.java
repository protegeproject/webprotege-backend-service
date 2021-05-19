package edu.stanford.protege.webprotege.itemlist;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.user.UserId;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsResult extends GetPossibleItemCompletionsResult<UserId> {

    /**
     * For serialization only
     */
    private GetUserIdCompletionsResult() {
    }

    private GetUserIdCompletionsResult(List<UserId> possibleItemCompletions) {
        super(possibleItemCompletions);
    }

    public static GetUserIdCompletionsResult create(List<UserId> possibleItemCompletions) {
        return new GetUserIdCompletionsResult(possibleItemCompletions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPossibleItemCompletions());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetUserIdCompletionsResult)) {
            return false;
        }
        GetUserIdCompletionsResult other = (GetUserIdCompletionsResult) obj;
        return this.getPossibleItemCompletions().equals(other.getPossibleItemCompletions());
    }


    @Override
    public String toString() {
        return toStringHelper("GetUserIdCompletionsResult")
                .addValue(getPossibleItemCompletions())
                .toString();
    }
}
