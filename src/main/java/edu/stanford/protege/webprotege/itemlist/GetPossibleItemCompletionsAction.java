package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.dispatch.Action;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public abstract class GetPossibleItemCompletionsAction<T> implements Action<GetPossibleItemCompletionsResult<T>> {

    private String completionText;

    protected GetPossibleItemCompletionsAction() {
    }

    public GetPossibleItemCompletionsAction(String completionText) {
        this.completionText = checkNotNull(completionText);
    }

    public String getCompletionText() {
        return completionText;
    }
}
