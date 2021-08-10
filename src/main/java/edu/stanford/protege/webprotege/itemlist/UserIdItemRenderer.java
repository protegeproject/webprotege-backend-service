package edu.stanford.protege.webprotege.itemlist;

import edu.stanford.protege.webprotege.common.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class UserIdItemRenderer implements ItemRenderer<UserId> {

    @Override
    public String getDisplayString(UserId item) {
        return item.id();
    }

    @Override
    public String getReplacementString(UserId item) {
        return item.id();
    }
}
