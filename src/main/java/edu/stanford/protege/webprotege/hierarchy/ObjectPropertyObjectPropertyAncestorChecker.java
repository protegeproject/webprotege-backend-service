package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import jakarta.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class ObjectPropertyObjectPropertyAncestorChecker implements HasHasAncestor<OWLObjectProperty, OWLObjectProperty> {

    private final ObjectPropertyHierarchyProvider hierarchyProvider;

    @Inject
    public ObjectPropertyObjectPropertyAncestorChecker(ObjectPropertyHierarchyProvider
                                                               hierarchyProvider) {
        this.hierarchyProvider = hierarchyProvider;
    }

    @Override
    public boolean hasAncestor(OWLObjectProperty node, OWLObjectProperty node2) {
        return node.equals(node2) || hierarchyProvider.getAncestors(node).contains(node2);
    }
}
