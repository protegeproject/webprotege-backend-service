package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import java.util.*;


@JsonTypeName(GetClassAncestorsAction.CHANNEL)
public class GetClassAncestorsResult implements Result {


    private final Set<OWLEntityData> ancestors;

    public GetClassAncestorsResult(Set<OWLEntityData> ancestors) {
        this.ancestors = ancestors;
    }

    @JsonProperty("ancestors")
    public Set<OWLEntityData> getAncestorTree() {
        return ancestors;
    }
}
