package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.sharing.PersonId;

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
