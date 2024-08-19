package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */


@JsonTypeName("webprotege.forms.GetEntityFormDescriptor")
public record GetEntityFormDescriptorResult(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                            @JsonProperty("formId") @Nonnull FormId formId,
                                            @JsonProperty("formDescriptor") @Nullable FormDescriptor formDescriptor,
                                            @JsonProperty("purpose") @Nonnull FormPurpose purpose,
                                            @Nullable CompositeRootCriteria selectorCriteria) implements Response {

}
