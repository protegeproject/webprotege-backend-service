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
    private final CommandExecutor<GetPostcoordinationAxisToGenericScaleRequest, GetPostcoordinationAxisToGenericScaleResponse> getPostcoordinationAxisToGenericScale;

    public PostcoordinationManagerImpl(CommandExecutor<CreatePostcoordinationFromParentRequest, CreatePostcoordinationFromParentResponse> createPostcoordinationFromParent,
                                       CommandExecutor<GetPostcoordinationAxisToGenericScaleRequest, GetPostcoordinationAxisToGenericScaleResponse> getPostcoordinationAxisToGenericScale) {
        this.createPostcoordinationFromParent = createPostcoordinationFromParent;
        this.getPostcoordinationAxisToGenericScale = getPostcoordinationAxisToGenericScale;
    }

    @Override
    public CompletableFuture<CreatePostcoordinationFromParentResponse> createPostcoordinationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId projectId, ExecutionContext executionContext) {
        return createPostcoordinationFromParent.execute(
                CreatePostcoordinationFromParentRequest.create(newEntityIri, parentEntityIri, projectId),
                executionContext
        );
    }

    @Override
    public CompletableFuture<GetPostcoordinationAxisToGenericScaleResponse> getPostcoordinationAxisToGenericScale(ExecutionContext executionContext) {
        return getPostcoordinationAxisToGenericScale.execute(
                new GetPostcoordinationAxisToGenericScaleRequest(),
                executionContext
        );
    }
}
