package edu.stanford.protege.webprotege.user;

import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.dispatch.Action;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
public class GetUserIdsAction implements Action<GetUserIdsResult> {

    public static final String CHANNEL = "users.GetUserIds";

    public GetUserIdsAction() {
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public int hashCode() {
        return "GetUserIdsAction".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof GetUserIdsAction;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetUserIdsAction")
                          .toString();
    }
}
