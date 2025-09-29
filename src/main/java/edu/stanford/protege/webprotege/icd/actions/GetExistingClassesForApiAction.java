package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.search.DeprecatedEntitiesTreatment;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;


@JsonTypeName(GetExistingClassesForApiAction.CHANNEL)
public record GetExistingClassesForApiAction(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                             @JsonProperty("searchString") @Nonnull String searchString,
                                             @JsonProperty("entityTypes") @Nonnull Set<EntityType<?>> entityTypes,
                                             @JsonProperty("langTagFilter") @Nonnull LangTagFilter langTagFilter,
                                             @JsonProperty("searchFilters") @Nonnull ImmutableList<EntitySearchFilter> searchFilters,
                                             @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest,
                                             @JsonProperty("resultsSetFilter") @Nullable EntityMatchCriteria resultsSetFilter,
                                             @JsonProperty("deprecatedEntitiesTreatment") DeprecatedEntitiesTreatment deprecatedEntitiesTreatment) implements ProjectAction<GetExistingClassesForApiResult> {
    public static final String CHANNEL = "webprotege.entities.GetExistingClassesForApi";


    @Override
    public String getChannel() {
        return CHANNEL;
    }




}
