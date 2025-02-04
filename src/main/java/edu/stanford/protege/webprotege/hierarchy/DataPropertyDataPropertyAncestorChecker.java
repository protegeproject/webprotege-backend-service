package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.OWLDataProperty;

import jakarta.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class DataPropertyDataPropertyAncestorChecker implements HasHasAncestor<OWLDataProperty, OWLDataProperty> {

    private final HierarchyProvider<OWLDataProperty> hierarchyProvider;

    @Inject
    public DataPropertyDataPropertyAncestorChecker(HierarchyProvider<OWLDataProperty>
                                                           hierarchyProvider) {
        this.hierarchyProvider = hierarchyProvider;
    }

    @Override
    public boolean hasAncestor(OWLDataProperty node, OWLDataProperty node2) {
        return node.equals(node2) || hierarchyProvider.getAncestors(node).contains(node2);
    }
}
