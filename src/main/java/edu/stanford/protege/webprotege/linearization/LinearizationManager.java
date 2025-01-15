package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface LinearizationManager {
    CompletableFuture<MergeWithParentEntitiesResponse> mergeLinearizationsFromParents(IRI currentEntityIri, Set<IRI> parentEntityIris, ProjectId id, ExecutionContext executionContext);

    CompletableFuture<CreateLinearizationFromParentResponse> createLinearizationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId id, ExecutionContext executionContext);
    CompletableFuture<Optional<IRI>> getParentThatIsLinearizationPathParent(IRI owlIri, Set<IRI> parentsIris, ProjectId id, ExecutionContext executionContext);

}
