package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@JsonTypeName(ValidateAxisBelongsToHierarchyAction.CHANNEL)
public record ValidateAxisBelongsToHierarchyAction(
        @JsonProperty("projectId") @Nonnull ProjectId projectId,
        @JsonProperty("hierarchyRootsToEntities") @Nonnull Map<IRI, List<IRI>> hierarchyRootsToEntities
) implements ProjectAction<ValidateAxisBelongsToHierarchyResult> {
    
    public static final String CHANNEL = "webprotege.icd.ValidateAxisBelongsToHierarchy";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
