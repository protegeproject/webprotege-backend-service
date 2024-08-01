package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */


@JsonTypeName("webprotege.forms.GetEntityDeprecationForms")
public record GetEntityDeprecationFormsResult(@Nonnull ImmutableList<FormDescriptorDto> formDtos,
                                              long referencesCount,
                                              @Nullable CompositeRootCriteria replacementEntityCriteria) implements Response {


    @JsonIgnore
    @Nonnull
    public Optional<CompositeRootCriteria> getReplacedByFilterCriteria() {
        return Optional.ofNullable(replacementEntityCriteria);
    }
}
