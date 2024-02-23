package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
public class RemoveAxiomsDelegateHandler extends AbstractProjectActionHandler<RemoveAxiomsRequest, RemoveAxiomsResponse> {

    private final ChangeManager changeManager;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    public RemoveAxiomsDelegateHandler(@Nonnull AccessManager accessManager,
                                       ChangeManager changeManager,
                                       DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.changeManager = changeManager;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @Nonnull
    @Override
    public Class<RemoveAxiomsRequest> getActionClass() {
        return RemoveAxiomsRequest.class;
    }

    @Nonnull
    @Override
    public RemoveAxiomsResponse execute(@Nonnull RemoveAxiomsRequest action,
                                        @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var loader = new AxiomsDocumentLoader(projectId,
                                              action.ontologyDocument(),
                                              action.mimeType(),
                                              defaultOntologyIdManager.getDefaultOntologyId());
        var changes = loader.<OntologyChange>loadAxioms((ax, ontologyId) -> new RemoveAxiomChange(ontologyId, ax));
        var result = changeManager.applyChanges(executionContext.userId(),
                                                new FixedChangeListGenerator<>(action.changeRequestId(), changes, "", action.commitMessage()));
        var appliedChangesCount = result.getChangeList().size();
        return new RemoveAxiomsResponse(projectId,
                                        appliedChangesCount);
    }
}
