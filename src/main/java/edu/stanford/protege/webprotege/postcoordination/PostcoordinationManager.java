package edu.stanford.protege.webprotege.postcoordination;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;

import java.util.concurrent.CompletableFuture;

public interface PostcoordinationManager {
    CompletableFuture<CreatePostcoordinationFromParentResponse> createPostcoordinationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId id, ExecutionContext executionContext);

}
