package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeGenerationContext;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.diff.OntologyDiff2OntologyChanges;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.owlapi.RenameMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
public class OntologyPatcher {

    @Nonnull
    private final HasApplyChanges changeManager;

    @Nonnull
    private final OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges;

    @Inject
    public OntologyPatcher(@Nonnull HasApplyChanges changeManager,
                           @Nonnull OntologyDiff2OntologyChanges ontologyDiff2OntologyChanges) {
        this.changeManager = checkNotNull(changeManager);
        this.ontologyDiff2OntologyChanges = checkNotNull(ontologyDiff2OntologyChanges);
    }

    public void applyPatch(@Nonnull ChangeRequestId changeRequestId,
                           @Nonnull Collection<OntologyDiff> diffSet,
                           @Nonnull String commitMessage,
                           @Nonnull ExecutionContext executionContext) {
        var changeList = new ArrayList<OntologyChange>();
        for(OntologyDiff diff : diffSet) {
            List<OntologyChange> changes = ontologyDiff2OntologyChanges.getOntologyChangesFromDiff(diff);
            changeList.addAll(changes);
        }
        applyChanges(changeRequestId, commitMessage, changeList, executionContext);
    }

    private void applyChanges(ChangeRequestId changeRequestId,
                              String commitMessage,
                              final List<OntologyChange> changes,
                              ExecutionContext executionContext) {
        changeManager.applyChanges(executionContext.userId(), new ChangeListGenerator<Boolean>() {
            @Override
            public ChangeRequestId getChangeRequestId() {
                return changeRequestId;
            }

            @Override
            public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
                OntologyChangeList.Builder<Boolean> builder = OntologyChangeList.builder();
                builder.addAll(changes);
                return builder.build(!changes.isEmpty());
            }

            @Override
            public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
                return true;
            }

            @Nonnull
            @Override
            public String getMessage(ChangeApplicationResult<Boolean> result) {
                return commitMessage;
            }
        });

    }
}
