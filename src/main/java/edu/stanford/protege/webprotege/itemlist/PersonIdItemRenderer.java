package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.sharing.PersonId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class PersonIdItemRenderer implements ItemRenderer<PersonId> {

    @Override
    public String getDisplayString(PersonId item) {
        return item.getId();
    }

    @Override
    public String getReplacementString(PersonId item) {
        return item.getId();
    }
}
