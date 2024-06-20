package edu.stanford.protege.webprotege.icd.hierarchy;

import org.semanticweb.owlapi.model.*;

import java.util.Set;

public interface ClassHierarchyRetiredClassDetector {

    boolean hasRetiredAncestor(OWLClass owlClass);

    Set<OWLClass> getRetiredAncestors(OWLClass owlClass);

    Set<OWLClass> getClassesWithRetiredAncestors(Set<OWLClass> owlClasses);

    boolean isRetired(OWLClass owlClass);
}
