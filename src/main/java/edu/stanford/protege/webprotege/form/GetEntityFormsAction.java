package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.form.data.FormRegionFilter;
import edu.stanford.protege.webprotege.form.field.FormRegionOrdering;
import edu.stanford.protege.webprotege.lang.LangTagFilter;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
@AutoValue

@JsonTypeName("GetEntityForms")
public abstract class GetEntityFormsAction implements ProjectAction<GetEntityFormsResult> {


    public static final String PROJECT_ID = "projectId";

    public static final String ENTITY = "entity";

    public static final String FORM_REGION_FILTERS = "formRegionFilters";

    public static final String PAGE_REQUESTS = "pageRequests";

    public static final String LANG_TAG_FILTER = "langTagFilter";

    public static final String REGION_ORDERINGS = "regionOrderings";

    private static final String FORM_FILTERS = "formFilters";

    /**
     * Get the forms for an entity
     *
     * @param projectId        The project id
     * @param entity           The entity
     * @param filters       A list of {@link FormId}s.  If the list is empty then all forms that are applicable
     *                         to the entity will be retrieved.  If the list is non-empty then the only the applicable
     *                         forms that have form Ids in the list will be retrieved.
     * @param formPageRequests A list of page requests pertaining to various regions on the form.
     * @param langTagFilter    A language tag filter that can be used to filter data in a specific language.
     * @param orderings        A set of region orderings that can be used to specify the ordering of specific regions of
     * @param filters          A set of region filters that can be used to filter values
     */
    @JsonCreator
    public static GetEntityFormsAction create(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                              @JsonProperty(ENTITY) @Nonnull OWLEntity entity,
                                              @JsonProperty(PAGE_REQUESTS) @Nonnull ImmutableSet<FormPageRequest> formPageRequests,
                                              @JsonProperty(LANG_TAG_FILTER) @Nonnull LangTagFilter langTagFilter,
                                              @JsonProperty(REGION_ORDERINGS) @Nonnull ImmutableSet<FormRegionOrdering> orderings,
                                              @JsonProperty(FORM_FILTERS) @Nonnull ImmutableSet<FormId> filters,
                                              @JsonProperty(FORM_REGION_FILTERS) ImmutableSet<FormRegionFilter> formRegionFilters) {
        return new AutoValue_GetEntityFormsAction(projectId,
                                                  entity,
                                                  formPageRequests,
                                                  langTagFilter,
                                                  orderings,
                                                  filters,
                                                  formRegionFilters);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    @JsonProperty(PAGE_REQUESTS)
    public abstract ImmutableSet<FormPageRequest> getFormPageRequests();

    @Nonnull
    @JsonProperty(LANG_TAG_FILTER)
    public abstract LangTagFilter getLangTagFilter();

    @Nonnull
    @JsonProperty(REGION_ORDERINGS)
    public abstract ImmutableSet<FormRegionOrdering> getGridControlOrdering();

    @Nonnull
    @JsonProperty(FORM_FILTERS)
    public abstract ImmutableSet<FormId> getFormFilters();

    @JsonProperty(FORM_REGION_FILTERS)
    public abstract ImmutableSet<FormRegionFilter> getFormRegionFilters();
}

