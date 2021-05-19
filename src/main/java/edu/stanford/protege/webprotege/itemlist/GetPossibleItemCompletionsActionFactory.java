package edu.stanford.protege.webprotege.itemlist;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public interface GetPossibleItemCompletionsActionFactory<T> {

    GetPossibleItemCompletionsAction<T> createGetPossibleItemCompletionsAction(String currentItemName);
}
