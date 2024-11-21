package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

@JsonTypeName(FilterExistingEntitiesAction.CHANNEL)
public record FilterExistingEntitiesAction(@JsonProperty("projectId") ProjectId projectId,
                                           @JsonProperty("iris") Set<IRI> iris) implements ProjectAction<FilterExistingEntitiesResult> {

    public static final String CHANNEL = "webprotege.entities.FilterExistingEntities";

    public static FilterExistingEntitiesAction create(ProjectId projectId, Set<IRI> iriSet) {
        return new FilterExistingEntitiesAction(projectId, iriSet);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
