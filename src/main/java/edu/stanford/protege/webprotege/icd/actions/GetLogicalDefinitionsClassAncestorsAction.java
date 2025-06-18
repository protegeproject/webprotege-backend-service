package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

public record GetLogicalDefinitionsClassAncestorsAction(IRI classIri, ProjectId projectId) implements ProjectAction<GetLogicalDefinitionsClassAncestorsResult> {

    public final static String CHANNEL = "webprotege.entities.GetLogicalDefinitionsClassAncestors";

    @JsonCreator
    public GetLogicalDefinitionsClassAncestorsAction(@JsonProperty("classIri") IRI classIri, @JsonProperty("projectId") ProjectId projectId) {
        this.classIri = classIri;
        this.projectId = projectId;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public ProjectId projectId(){
        return projectId;
    }
}
