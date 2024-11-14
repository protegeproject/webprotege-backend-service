package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.hierarchy.*;
import org.semanticweb.owlapi.model.IRI;

import static edu.stanford.protege.webprotege.icd.actions.GetEntityChildrenAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetEntityChildrenAction(
        @JsonProperty("classIri") IRI classIri,
        @JsonProperty("projectId") ProjectId projectId,
        @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor
) implements ProjectAction<GetEntityChildrenResult> {
    public static final String CHANNEL = "webprotege.entities.GetEntityChildren";


    public static GetEntityChildrenAction create(IRI classIri,
                                                 ProjectId projectId) {
        return new GetEntityChildrenAction(classIri, projectId, ClassHierarchyDescriptor.create());
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
