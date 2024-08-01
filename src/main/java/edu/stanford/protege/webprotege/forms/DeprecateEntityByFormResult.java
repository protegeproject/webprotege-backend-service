package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Response;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
@JsonTypeName("webprotege.forms.DeprecateEntityByForm")
public record DeprecateEntityByFormResult() implements Response {

}
