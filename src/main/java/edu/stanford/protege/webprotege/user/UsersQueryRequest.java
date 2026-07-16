package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.Request;

/**
 * Mirrors the request contract owned by the user-management service on this channel: the
 * search term is serialized as {@code completionText} and {@code exactMatch} selects between
 * exact resolution and completion-style substring search.  The service binds strictly by
 * these JSON names - a differently-named term field arrives as {@code null}, which the
 * service treats as an unfiltered query that returns every user in the realm.
 */
public record UsersQueryRequest(@JsonProperty("completionText") String completionText,
                                @JsonProperty("exactMatch") boolean exactMatch) implements Request<UsersQueryResponse> {

    public final static String CHANNEL = "webprotege.usersquery.QueryUsers";

    @Override
    public String getChannel() {
        return CHANNEL;
    }


}
