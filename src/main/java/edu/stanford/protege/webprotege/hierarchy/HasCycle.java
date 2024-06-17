package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;

import java.util.List;

public interface HasCycle {
    boolean hasCycle(List<OntologyChange> changes);
}
