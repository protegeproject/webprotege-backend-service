package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.icd.actions.AncestorHierarchyNode;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface ClassHierarchyProvider extends HierarchyProvider<OWLClass> {

  void handleChanges(List<OntologyChange> changes);

  List<OntologyChange> filterIrrelevantChanges(List<OntologyChange> changes);

  AncestorHierarchyNode<OWLClass> getAncestorsTree(OWLClass object);

}
