package edu.stanford.protege.webprotege.postcoordination;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.*;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class PostcoordinationManagerImpl implements PostcoordinationManager {

    private final Logger logger = LoggerFactory.getLogger(PostcoordinationManagerImpl.class);

    private final CommandExecutor<CreatePostcoordinationFromParentRequest, CreatePostcoordinationFromParentResponse> createPostcoordinationFromParent;

    public PostcoordinationManagerImpl(CommandExecutor<CreatePostcoordinationFromParentRequest, CreatePostcoordinationFromParentResponse> createPostcoordinationFromParent) {
        this.createPostcoordinationFromParent = createPostcoordinationFromParent;
    }

    @Override
    public CompletableFuture<CreatePostcoordinationFromParentResponse> createPostcoordinationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId projectId, ExecutionContext executionContext) {
        return createPostcoordinationFromParent.execute(
                CreatePostcoordinationFromParentRequest.create(newEntityIri, parentEntityIri, projectId),
                executionContext
        );
    }
}
