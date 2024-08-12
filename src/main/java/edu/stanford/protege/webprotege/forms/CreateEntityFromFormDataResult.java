package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableCollection;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-12
 */
@JsonTypeName("webprotege.forms.CreateEntityFromFormData")
public record CreateEntityFromFormDataResult(@Nonnull ProjectId projectId,
                                             ImmutableCollection<OWLEntityData> entities) implements Response {

}
