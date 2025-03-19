package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.Response;

import javax.annotation.Nonnull;
import java.util.List;

@JsonTypeName(GetEntityDirectParentsAction.CHANNEL)
public record GetEntityDirectParentsResult(
        @JsonProperty("entity") @Nonnull OWLEntityData entity,
        @JsonProperty("directParents") List<EntityNode> directParents
) implements Response {

}
