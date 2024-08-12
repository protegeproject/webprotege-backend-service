package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */


@JsonTypeName("webprotege.forms.GetProjectFormDescriptors")
public record GetProjectFormDescriptorsResult(@Nonnull ProjectId projectId,
                                              @Nonnull ImmutableList<FormDescriptor> formDescriptors,
                                              @Nonnull ImmutableList<EntityFormSelector> formSelectors) implements Response {

}
