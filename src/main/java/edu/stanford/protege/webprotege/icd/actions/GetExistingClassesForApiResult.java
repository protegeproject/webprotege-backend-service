package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;

public record GetExistingClassesForApiResult(Page<ExistingClasses> existingClassesList) implements Result {


    public static record ExistingClasses(String browserText, IRI iri){}
}
