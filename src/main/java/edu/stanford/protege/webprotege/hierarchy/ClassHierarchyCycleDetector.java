package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.*;

public interface ClassHierarchyCycleDetector extends HasCycle{

    Set<OWLClass> getClassesWithCycle(List<OntologyChange> changes);
}
