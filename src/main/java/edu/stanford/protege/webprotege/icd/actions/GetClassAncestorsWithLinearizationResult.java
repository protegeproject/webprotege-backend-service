package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.entity.OWLEntityData;

import java.util.*;

import static edu.stanford.protege.webprotege.icd.actions.GetClassAncestorsWithLinearizationAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetClassAncestorsWithLinearizationResult(@JsonProperty("ancestorsWithLinearization") List<OWLEntityData> ancestorsWithLinearization) implements Result {

    @JsonCreator
    public static GetClassAncestorsWithLinearizationResult create(@JsonProperty("ancestorsWithLinearization") List<OWLEntityData> ancestorsWithLinearization){
        return new GetClassAncestorsWithLinearizationResult(ancestorsWithLinearization);
    }

}
