package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("PerformEntitySearch")
public abstract class PerformEntitySearchAction implements ProjectAction<PerformEntitySearchResult>, HasProjectId {

    @JsonCreator
    public static PerformEntitySearchAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                   @JsonProperty("searchString") @Nonnull String searchString,
                                                   @JsonProperty("entityTypes") @Nonnull Set<EntityType<?>> entityTypes,
                                                   @JsonProperty("langTagFilter") @Nonnull LangTagFilter langTagFilter,
                                                   @JsonProperty("searchFilters") @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                                   @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest) {
        return new AutoValue_PerformEntitySearchAction(projectId,
                                             entityTypes,
                                                       searchString,
                                                       langTagFilter,
                                             searchFilters,
                                             pageRequest);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract Set<EntityType<?>> getEntityTypes();

    @Nonnull
    public abstract String getSearchString();

    @Nonnull
    public abstract LangTagFilter getLangTagFilter();

    @Nonnull
    public abstract ImmutableList<EntitySearchFilter> getSearchFilters();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
