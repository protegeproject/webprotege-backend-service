package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.common.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsActionFactory implements GetPossibleItemCompletionsActionFactory<UserId> {

    @Override
    public GetPossibleItemCompletionsAction<UserId> createGetPossibleItemCompletionsAction(String currentItemName) {
        return GetUserIdCompletionsAction.create(currentItemName);
    }
}
