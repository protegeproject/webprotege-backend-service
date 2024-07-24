package edu.stanford.protege.webprotege.lineariazation;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.*;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class LinearizationManagerImpl implements LinearizationManager {

    private final CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeLinearizationsExecutor;

    private final Logger logger = LoggerFactory.getLogger(LinearizationManagerImpl.class);

    public LinearizationManagerImpl(CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeLinearizationsExecutor) {
        this.mergeLinearizationsExecutor = mergeLinearizationsExecutor;
    }


    @Override
    public CompletableFuture<MergeWithParentEntitiesResponse> mergeLinearizationsFromParents(IRI currentEntityIri, Set<IRI> parentEntityIris, ProjectId projectId, ExecutionContext executionContext) {
        return mergeLinearizationsExecutor.execute(
                MergeWithParentEntitiesRequest.create(currentEntityIri, parentEntityIris, projectId),
                executionContext
        );
    }
}
