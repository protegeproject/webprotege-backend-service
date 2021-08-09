package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
@AutoValue

@JsonTypeName("GetDeprecatedEntities")
public abstract class GetDeprecatedEntitiesAction implements ProjectAction<GetDeprecatedEntitiesResult> {

    @JsonCreator
    public static GetDeprecatedEntitiesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                       @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest,
                                       @JsonProperty("entityTypes") @Nonnull Set<EntityType<?>> entityTypes) {
        return new AutoValue_GetDeprecatedEntitiesAction(projectId, pageRequest, entityTypes);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract PageRequest getPageRequest();

    public abstract Set<EntityType<?>> getEntityTypes();
}
