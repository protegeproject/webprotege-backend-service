package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.forms.data.FormRegionFilter;
import edu.stanford.protege.webprotege.forms.field.FormRegionOrdering;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */

/**
 * Get the forms for an term
 *
 * @param projectId        The project id
 * @param entity           The term
 * @param formFilters          A list of {@link FormId}s.  If the list is empty then all forms that are applicable
 *                         to the term will be retrieved.  If the list is non-empty then the only the applicable
 *                         forms that have form Ids in the list will be retrieved.
 * @param formPageRequests A list of page requests pertaining to various regions on the form.
 * @param langTagFilter    A language tag filter that can be used to filter data in a specific language.
 * @param formRegionOrderings        A set of region formRegionOrderings that can be used to specify the ordering of specific regions of
 * @param formFilters          A set of region formFilters that can be used to filter values
 */
@JsonTypeName("webprotege.forms.GetEntityForms")
public record GetEntityFormsAction(@Nonnull ProjectId projectId,
                                   @JsonProperty("entity") @Nonnull OWLEntity entity,
                                   @JsonProperty("formPageRequests") @Nonnull ImmutableSet<FormPageRequest> formPageRequests,
                                   @JsonProperty("langTagFilter") @Nonnull LangTagFilter langTagFilter,
                                   @JsonProperty("formRegionOrderings") @Nonnull ImmutableSet<FormRegionOrdering> formRegionOrderings,
                                   @JsonProperty("formFilters") @Nonnull ImmutableSet<FormId> formFilters,
                                   @JsonProperty("formRegionFilters") ImmutableSet<FormRegionFilter> formRegionFilters) implements ProjectRequest<GetEntityFormsResult> {

    public static final String CHANNEL = "webprotege.forms.GetEntityForms";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}

