package edu.stanford.protege.webprotege.itemlist;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.sharing.PersonId;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsResult extends GetPossibleItemCompletionsResult<PersonId> {

    /**
     * For serialization only
     */
    private GetPersonIdCompletionsResult() {
    }

    private GetPersonIdCompletionsResult(List<PersonId> possibleItemCompletions) {
        super(possibleItemCompletions);
    }

    public static GetPersonIdCompletionsResult create(List<PersonId> possibleItemCompletions) {
        return new GetPersonIdCompletionsResult(possibleItemCompletions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPossibleItemCompletions());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdCompletionsResult")
                .addValue(getPossibleItemCompletions())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdCompletionsResult)) {
            return false;
        }
        GetPersonIdCompletionsResult other = (GetPersonIdCompletionsResult) obj;
        return this.getPossibleItemCompletions().equals(other.getPossibleItemCompletions());
    }
}
