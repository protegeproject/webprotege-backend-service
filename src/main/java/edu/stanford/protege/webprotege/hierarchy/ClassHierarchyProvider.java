package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 */
public interface ClassHierarchyProvider extends HierarchyProvider<OWLClass> {

    void handleChanges(List<OntologyChange> changes);

    List<OntologyChange> filterIrrelevantChanges(List<OntologyChange> changes);

    Stream<OWLClass> getParentsStreamFromSubClassOf(OWLClass owlClass);

    Stream<OWLClass> getParentsStreamFromEquivalentClass(OWLClass owlClass);

}
