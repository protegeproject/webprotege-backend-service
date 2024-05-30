package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.Request;

public record UsersQueryRequest(String userName) implements Request<UsersQueryResponse> {

    public final static String CHANNEL = "webprotege.usersquery.QueryUsers";

    @Override
    public String getChannel() {
        return CHANNEL;
    }


}
