package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface ClassHierarchyProvider extends HierarchyProvider<OWLClass> {

  void handleChanges(List<OntologyChange> changes);

  boolean hasCycle(List<OntologyChange> changes);

  Set<OWLClass> getClassesWithCycle(List<OntologyChange> changes);
}
