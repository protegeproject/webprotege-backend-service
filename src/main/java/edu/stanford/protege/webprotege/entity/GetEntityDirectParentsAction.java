package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static edu.stanford.protege.webprotege.entity.GetEntityDirectParentsAction.CHANNEL;


@JsonTypeName(CHANNEL)
public record GetEntityDirectParentsAction(
        @JsonProperty("projectId") @Nonnull ProjectId projectId,
        @JsonProperty("entity") @Nonnull OWLEntity entity
) implements ProjectRequest<GetEntityDirectParentsResult> {
    public static final String CHANNEL = "webprotege.hierarchies.GetEntityDirectParents";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
