package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
@JsonTypeName("webprotege.forms.SetProjectFormDescriptors")
public record SetProjectFormDescriptorsResult() implements Response {

}
