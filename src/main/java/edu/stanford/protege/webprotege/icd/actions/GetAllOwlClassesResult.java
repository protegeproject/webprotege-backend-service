package edu.stanford.protege.webprotege.icd.actions;


import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;

@JsonTypeName(GetAllOwlClassesAction.CHANNEL)
public record GetAllOwlClassesResult(List<IRI> owlClassList) implements Result {
}
