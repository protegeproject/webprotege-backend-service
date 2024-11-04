package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.*;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static edu.stanford.protege.webprotege.icd.actions.GetClassAncestorsWithLinearizationAction.CHANNEL;

@JsonTypeName(CHANNEL)
public class GetClassAncestorsWithLinearizationActionHandler extends AbstractProjectActionHandler<GetClassAncestorsWithLinearizationAction, GetClassAncestorsWithLinearizationResult> {

    private static final Logger logger = LoggerFactory.getLogger(AccessManagerImpl.class);


    private final ClassHierarchyProvider classHierarchyProvider;

    private final RenderingManager renderingManager;

    private final LinearizationManager linearizationManager;

    public GetClassAncestorsWithLinearizationActionHandler(@NotNull AccessManager accessManager,
                                                           ClassHierarchyProvider classHierarchyProvider,
                                                           RenderingManager renderingManager,
                                                           LinearizationManager linearizationManager) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.renderingManager = renderingManager;
        this.linearizationManager = linearizationManager;
    }

    @NotNull
    @Override
    public Class<GetClassAncestorsWithLinearizationAction> getActionClass() {
        return GetClassAncestorsWithLinearizationAction.class;
    }

    @NotNull
    @Override
    public GetClassAncestorsWithLinearizationResult execute(@NotNull GetClassAncestorsWithLinearizationAction action, @NotNull ExecutionContext executionContext) {
        var ancestors = classHierarchyProvider.getAncestors(new OWLClassImpl(action.classIri()));

        var ancestorIris = ancestors.stream()
                .filter(ancestor -> !ancestor.getIRI().equals(action.classIri()))
                .map(OWLEntity::toStringID)
                .toList();

        try {
            List<String> irisWithLinearization = linearizationManager.getIrisWithLinearization(ancestorIris, action.projectId(), executionContext).get().iris();
            var ancestorsWithLinearization = ancestors.stream()
                    .filter(ancestor -> irisWithLinearization.contains(ancestor.getIRI().toString()))
                    .map(renderingManager::getRendering)
                    .toList();

            return GetClassAncestorsWithLinearizationResult.create(ancestorsWithLinearization);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String errorMsg = String.format("Thread was interrupted while processing IRIs for project ID %s", action.projectId());
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            String errorMsg = String.format("Execution exception occurred for project ID %s with ancestor IRIs %s. Cause: %s",
                    action.projectId(), ancestorIris, cause != null ? cause.getMessage() : "Unknown cause");
            logger.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }
}
