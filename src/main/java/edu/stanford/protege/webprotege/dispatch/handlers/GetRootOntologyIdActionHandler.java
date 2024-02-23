package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ontology.GetRootOntologyIdAction;
import edu.stanford.protege.webprotege.ontology.GetRootOntologyIdResult;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetRootOntologyIdActionHandler extends AbstractProjectActionHandler<GetRootOntologyIdAction, GetRootOntologyIdResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;


    @Inject
    public GetRootOntologyIdActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectId projectId,
                                          @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.projectId = projectId;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    /**
     * Gets the class of {@link Action} handled by this handler.
     * @return The class of {@link Action}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public Class<GetRootOntologyIdAction> getActionClass() {
        return GetRootOntologyIdAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetRootOntologyIdAction action) {
        return VIEW_PROJECT;
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param executionContext The {@link edu.stanford.protege.webprotege.ipc.ExecutionContext;} that should be
     * used to provide details such as the
     * {@link UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Nonnull
    @Override
    public GetRootOntologyIdResult execute(@Nonnull GetRootOntologyIdAction action, @Nonnull ExecutionContext executionContext) {
        var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
        return new GetRootOntologyIdResult(projectId, ontologyId);
    }
}
