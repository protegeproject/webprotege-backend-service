package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Response;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-28
 */


@JsonTypeName("webprotege.forms.GetEntityCreationForms")
public record GetEntityCreationFormsResult(ImmutableList<FormDescriptorDto> formDescriptors) implements Response {

}
