package edu.stanford.protege.webprotege.forms;


import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

@JsonTypeName(SetEntityFormDataFromJsonAction.CHANNEL)
public record SetEntityFormDataFromJsonResult() implements Response {
}
