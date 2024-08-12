package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.forms.data.FormDataDto;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */


@JsonTypeName("webprotege.forms.GetEntityForms")
public record GetEntityFormsResult(@JsonProperty("entity") OWLEntityData entityData,
                                   @JsonProperty("filteredFormIds") ImmutableList<FormId> filteredFormIds,
                                   @JsonProperty("formData") ImmutableList<FormDataDto> formData) implements Response {

}
