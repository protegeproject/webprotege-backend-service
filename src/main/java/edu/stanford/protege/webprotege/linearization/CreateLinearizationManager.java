package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;

public interface CreateLinearizationManager {

    boolean createLinearizationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId id, ExecutionContext executionContext);
}
