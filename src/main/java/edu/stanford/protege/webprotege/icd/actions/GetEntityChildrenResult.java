package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;

import static edu.stanford.protege.webprotege.icd.actions.GetEntityChildrenAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record GetEntityChildrenResult(@JsonProperty("childrenIris") List<IRI> childrenIris) implements Result {

    public static GetEntityChildrenResult create(List<IRI> childrenIris) {
        return new GetEntityChildrenResult(childrenIris);
    }
}
