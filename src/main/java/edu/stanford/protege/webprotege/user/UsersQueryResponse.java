package edu.stanford.protege.webprotege.user;

import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.common.UserId;

import java.util.List;

public record UsersQueryResponse(List<UserId> completions) implements Response {

}
