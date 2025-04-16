package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;


@JsonTypeName(GetClassAncestorsAction.CHANNEL)
public class GetClassAncestorsResult implements Result {


    private final AncestorHierarchyNode<OWLEntityData> ancestorTree;

    public GetClassAncestorsResult(AncestorHierarchyNode<OWLEntityData> ancestorTree) {
        this.ancestorTree = ancestorTree;
    }

    @JsonProperty("ancestorTree")
    public AncestorHierarchyNode<OWLEntityData> getAncestorTree() {
        return ancestorTree;
    }
}
