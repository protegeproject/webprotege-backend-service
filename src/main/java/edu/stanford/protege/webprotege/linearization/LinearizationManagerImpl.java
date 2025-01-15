package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class LinearizationManagerImpl implements LinearizationManager {

    private final Logger logger = LoggerFactory.getLogger(LinearizationManagerImpl.class);

    private final CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeLinearizationsExecutor;
    private final CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParent;
    private final CommandExecutor<GetParentThatIsLinearizationPathParentRequest, GetParentThatIsLinearizationPathParentResponse> isAnyParentLinearizationParent;

    public LinearizationManagerImpl(CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeLinearizationsExecutor,
                                    CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParent,
                                    CommandExecutor<GetParentThatIsLinearizationPathParentRequest, GetParentThatIsLinearizationPathParentResponse> isAnyParentLinearizationParent) {
        this.mergeLinearizationsExecutor = mergeLinearizationsExecutor;
        this.createLinearizationFromParent = createLinearizationFromParent;
        this.isAnyParentLinearizationParent = isAnyParentLinearizationParent;
    }


    @Override
    public CompletableFuture<MergeWithParentEntitiesResponse> mergeLinearizationsFromParents(IRI currentEntityIri, Set<IRI> parentEntityIris, ProjectId projectId, ExecutionContext executionContext) {
        return mergeLinearizationsExecutor.execute(
                MergeWithParentEntitiesRequest.create(currentEntityIri, parentEntityIris, projectId),
                executionContext
        );
    }

    @Override
    public CompletableFuture<CreateLinearizationFromParentResponse> createLinearizationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId projectId, ExecutionContext executionContext) {
        return createLinearizationFromParent.execute(
                CreateLinearizationFromParentRequest.create(newEntityIri, parentEntityIri, projectId),
                executionContext
        );
    }

    @Override
    public CompletableFuture<Optional<IRI>> getParentThatIsLinearizationPathParent(IRI owlIri, Set<IRI> parentsIris, ProjectId projectId, ExecutionContext executionContext) {
        return isAnyParentLinearizationParent.execute(
                GetParentThatIsLinearizationPathParentRequest.create(owlIri, parentsIris, projectId),
                executionContext
        ).thenApply(GetParentThatIsLinearizationPathParentResponse::parentAsLinearizationPathParent);
    }
}
