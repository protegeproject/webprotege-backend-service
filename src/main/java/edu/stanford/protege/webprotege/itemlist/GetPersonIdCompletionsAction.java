package edu.stanford.protege.webprotege.itemlist;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.sharing.PersonId;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsAction extends GetPossibleItemCompletionsAction<PersonId> {

    /**
     * For serialization only
     */
    private GetPersonIdCompletionsAction() {
    }

    private GetPersonIdCompletionsAction(String completionText) {
        super(completionText);
    }

    public static GetPersonIdCompletionsAction create(String completionText) {
        return new GetPersonIdCompletionsAction(completionText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCompletionText());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdCompletionsAction)) {
            return false;
        }
        GetPersonIdCompletionsAction other = (GetPersonIdCompletionsAction) obj;
        return this.getCompletionText().equals(other.getCompletionText());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdCompletionsAction")
                .addValue(getCompletionText())
                .toString();
    }
}
