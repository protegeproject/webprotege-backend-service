package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.Set;

public interface ClassHierarchyRetiredAncestorDetector {

    boolean hasRetiredAncestor(OWLClass owlClass);

    Set<OWLClass> getRetiredAncestors(OWLClass owlClass);

    Set<OWLClass> getClassesWithRetiredAncestors(Set<OWLClass> owlClasses);
}
