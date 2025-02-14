package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.OWLClass;

import jakarta.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class ClassClassAncestorChecker implements HasHasAncestor<OWLClass, OWLClass> {

    private final ClassHierarchyProvider hierarchyProvider;

    @Inject
    public ClassClassAncestorChecker(ClassHierarchyProvider hierarchyProvider) {
        this.hierarchyProvider = hierarchyProvider;
    }

    @Override
    public boolean hasAncestor(OWLClass descendant, OWLClass ancestor) {
        return descendant.equals(ancestor) || hierarchyProvider.isAncestor(descendant, ancestor);
    }
}
