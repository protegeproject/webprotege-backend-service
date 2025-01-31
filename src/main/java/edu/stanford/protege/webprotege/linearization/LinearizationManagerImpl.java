package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.*;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Component
public class LinearizationManagerImpl implements LinearizationManager {

    private final Logger logger = LoggerFactory.getLogger(LinearizationManagerImpl.class);

    private final CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeLinearizationsExecutor;
    private final CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParent;
    private final CommandExecutor<GetParentsThatAreLinearizationPathParents, GetParentsThatAreLinearizationPathParentsResponse> areParentsLinearizationParents;

    public LinearizationManagerImpl(CommandExecutor<MergeWithParentEntitiesRequest, MergeWithParentEntitiesResponse> mergeLinearizationsExecutor,
                                    CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParent,
                                    CommandExecutor<GetParentsThatAreLinearizationPathParents, GetParentsThatAreLinearizationPathParentsResponse> areParentsLinearizationParents) {
        this.mergeLinearizationsExecutor = mergeLinearizationsExecutor;
        this.createLinearizationFromParent = createLinearizationFromParent;
        this.areParentsLinearizationParents = areParentsLinearizationParents;
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
    public CompletableFuture<Set<IRI>> getParentsThatAreLinearizationPathParents(IRI owlIri, Set<IRI> parentsIris, ProjectId projectId, ExecutionContext executionContext) {
        return areParentsLinearizationParents.execute(
                GetParentsThatAreLinearizationPathParents.create(owlIri, parentsIris, projectId),
                executionContext
        ).thenApply(GetParentsThatAreLinearizationPathParentsResponse::parentsThatAreLinearizationPathParents);
    }
}
