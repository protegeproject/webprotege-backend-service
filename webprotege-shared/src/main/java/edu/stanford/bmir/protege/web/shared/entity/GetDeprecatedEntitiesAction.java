package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
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
