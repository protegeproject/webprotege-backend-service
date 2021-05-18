package edu.stanford.bmir.protege.web.server.itemlist;

import edu.stanford.bmir.protege.web.server.sharing.PersonId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsActionFactory implements GetPossibleItemCompletionsActionFactory<PersonId> {
    @Override
    public GetPossibleItemCompletionsAction<PersonId> createGetPossibleItemCompletionsAction(String currentItemName) {
        return GetPersonIdCompletionsAction.create(currentItemName);
    }
}
