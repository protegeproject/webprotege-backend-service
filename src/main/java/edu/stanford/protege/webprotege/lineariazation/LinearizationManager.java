package edu.stanford.protege.webprotege.lineariazation;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface LinearizationManager {
    CompletableFuture<MergeWithParentEntitiesResponse> mergeLinearizationsFromParents(IRI currentEntityIri, Set<IRI> parentEntityIris, ProjectId id, ExecutionContext executionContext);
}
