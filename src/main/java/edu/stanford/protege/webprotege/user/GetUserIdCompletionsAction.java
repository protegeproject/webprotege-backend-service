package edu.stanford.protege.webprotege.user;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Action;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsAction implements Action<GetUserIdCompletionsResult> {


    public static final String CHANNEL = "webprotege.users.GetUserIdCompletions";

    private final String completionText;

    private GetUserIdCompletionsAction(String completionText) {
        this.completionText = completionText;
    }

    public static GetUserIdCompletionsAction create(String completionText) {
        return new GetUserIdCompletionsAction(completionText);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public String getCompletionText() {
        return completionText;
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
        if (!(obj instanceof GetUserIdCompletionsAction)) {
            return false;
        }
        GetUserIdCompletionsAction other = (GetUserIdCompletionsAction) obj;
        return this.getCompletionText().equals(other.getCompletionText());
    }


    @Override
    public String toString() {
        return toStringHelper("GetUserIdCompletionsAction")
                .addValue(getCompletionText())
                .toString();
    }
}
