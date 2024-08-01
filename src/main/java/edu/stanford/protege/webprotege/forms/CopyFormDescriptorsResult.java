package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Response;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */


@JsonTypeName("webprotege.forms.CopyFormDescriptors")
public record CopyFormDescriptorsResult(ImmutableList<FormDescriptor> copiedFormDescriptors) implements Response {

}
