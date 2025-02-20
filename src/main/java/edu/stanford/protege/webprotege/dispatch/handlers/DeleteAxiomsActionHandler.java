package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.protege.webprotege.dispatch.actions.DeleteAxiomsResult;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 */
public class DeleteAxiomsActionHandler extends AbstractProjectActionHandler<DeleteAxiomsAction, DeleteAxiomsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public DeleteAxiomsActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull ChangeManager changeManager,
                                     @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.changeManager = checkNotNull(changeManager);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }

    @Nonnull
    @Override
    public Class<DeleteAxiomsAction> getActionClass() {
        return DeleteAxiomsAction.class;
    }

    @Nonnull
    @Override
    public DeleteAxiomsResult execute(@Nonnull DeleteAxiomsAction action, @Nonnull ExecutionContext executionContext) {
        var builder = OntologyChangeList.<String>builder();
        var ontId = defaultOntologyIdManager.getDefaultOntologyId();
        action.getAxioms()
              .forEach(ax -> builder.add(RemoveAxiomChange.of(ontId, ax)));
        var changeList = builder.build(action.getCommitMessage());
        var changeListGenerator = new FixedChangeListGenerator<>(action.changeRequestId(), changeList.getChanges(),
                                                                 "",
                                                                 action.getCommitMessage());
        var result = changeManager.applyChanges(executionContext.userId(),
                                                changeListGenerator);
        int removedAxioms = result.getChangeList()
                                  .size();
        return new DeleteAxiomsResult(projectId, removedAxioms);
    }
}
