package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

import static edu.stanford.protege.webprotege.icd.actions.FilterExistingEntitiesAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record FilterExistingEntitiesResult(
        @JsonProperty("existingEntities") Set<IRI> existingEntities) implements Result {
    public static FilterExistingEntitiesResult create(Set<IRI> existingEntities) {
        return new FilterExistingEntitiesResult(existingEntities);
    }
}
