package edu.stanford.protege.webprotege.linearization;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.*;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.*;
import org.springframework.stereotype.Component;

@Component
public class CreateLinearizationManagerImpl implements CreateLinearizationManager {

    private final CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParent;

    private final Logger logger = LoggerFactory.getLogger(CreateLinearizationManagerImpl.class);

    public CreateLinearizationManagerImpl(CommandExecutor<CreateLinearizationFromParentRequest, CreateLinearizationFromParentResponse> createLinearizationFromParent) {
        this.createLinearizationFromParent = createLinearizationFromParent;
    }


    @Override
    public boolean createLinearizationFromParent(IRI newEntityIri, IRI parentEntityIri, ProjectId projectId, ExecutionContext executionContext) {
        var response = createLinearizationFromParent.execute(
                CreateLinearizationFromParentRequest.create(newEntityIri, parentEntityIri, projectId),
                executionContext
        );
        return response.isDone();
    }
}
