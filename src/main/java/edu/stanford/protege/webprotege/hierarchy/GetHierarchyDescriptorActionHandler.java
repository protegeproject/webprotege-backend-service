package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class GetHierarchyDescriptorActionHandler extends AbstractProjectActionHandler<GetHierarchyDescriptorRequest, GetHierarchyDescriptorResponse> {


    private AccessManager accessManager;

    private Logger logger = LoggerFactory.getLogger(GetHierarchyDescriptorActionHandler.class);

    public GetHierarchyDescriptorActionHandler(@Nonnull AccessManager accessManager) {
        super(accessManager);
        this.accessManager = accessManager;
    }

    @Nonnull
    @Override
    public Class<GetHierarchyDescriptorRequest> getActionClass() {
        return GetHierarchyDescriptorRequest.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetHierarchyDescriptorRequest action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyDescriptorResponse execute(@Nonnull GetHierarchyDescriptorRequest action, @Nonnull ExecutionContext executionContext) {
        var displayContext = action.displayContext();
        logger.info("GetHierarchyDescriptorRequest: {}", action);
        var userId = executionContext.userId();
        var actionClosure = accessManager.getActionClosure(Subject.forUser(userId), ProjectResource.forProject(action.projectId()), executionContext);
        logger.info("GetHierarchyDescriptor: {}", actionClosure);
        return new GetHierarchyDescriptorResponse(ClassHierarchyDescriptor.create(
                Set.of(new OWLClassImpl(IRI.create("http://www.example.org/RBdOBCXUNWSHBJN16omh8h5")))
        ));
    }
}
