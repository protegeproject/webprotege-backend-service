package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface DataPropertyHierarchyProvider extends HierarchyProvider<OWLDataProperty> {

  void handleChanges(List<OntologyChange> changes);
}
