package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsResult implements Result {

    private final List<UserId> completions;

    private GetUserIdCompletionsResult(List<UserId> possibleItemCompletions) {
        completions = List.copyOf(possibleItemCompletions);
    }

    public static GetUserIdCompletionsResult create(List<UserId> possibleItemCompletions) {
        return new GetUserIdCompletionsResult(possibleItemCompletions);
    }

    public List<UserId> getCompletions() {
        return completions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetUserIdCompletionsResult that = (GetUserIdCompletionsResult) o;
        return Objects.equals(completions, that.completions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completions);
    }

    @Override
    public String toString() {
        return "GetUserIdCompletionsResult{" + "completions=" + completions + '}';
    }
}
