package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public record ValidateAxisBelongsToHierarchyResult(
        @JsonProperty("invalidEntitiesByRoot") @Nonnull Map<IRI, List<IRI>> invalidEntitiesByRoot
) implements Result {
    
    public static ValidateAxisBelongsToHierarchyResult create(@Nonnull Map<IRI, List<IRI>> invalidEntitiesByRoot) {
        return new ValidateAxisBelongsToHierarchyResult(invalidEntitiesByRoot);
    }
}
