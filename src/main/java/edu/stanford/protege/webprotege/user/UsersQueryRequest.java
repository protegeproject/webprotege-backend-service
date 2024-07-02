package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.Request;

@JsonTypeName(UsersQueryRequest.CHANNEL)
public record UsersQueryRequest(@JsonProperty("completionText") String userName, @JsonProperty("exactMatch") boolean exactMatch) implements Request<UsersQueryResponse> {

    public final static String CHANNEL = "webprotege.usersquery.QueryUsers";

    @Override
    public String getChannel() {
        return CHANNEL;
    }


}
