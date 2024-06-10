package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface ClassHierarchyProvider extends HierarchyProvider<OWLClass> {

  void handleChanges(List<OntologyChange> changes);

  static List<OntologyChange> filterIrrelevantChanges(List<OntologyChange> changes) {
    return changes.stream()
            .filter(OntologyChange::isAxiomChange)
            .collect(toList());
  }
}
