package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

import static edu.stanford.protege.webprotege.icd.actions.GetClassAncestorsWithLinearizationAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetClassAncestorsWithLinearizationAction(
        IRI classIri,
        ProjectId projectId
) implements ProjectAction<GetClassAncestorsWithLinearizationResult> {

    public final static String CHANNEL = "webprotege.entities.GetClassAncestorsWithLinearization";

    @JsonCreator
    public GetClassAncestorsWithLinearizationAction(@JsonProperty("classIri") IRI classIri, @JsonProperty("projectId") ProjectId projectId) {
        this.classIri = classIri;
        this.projectId = projectId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public ProjectId projectId() {
        return projectId;
    }

}
