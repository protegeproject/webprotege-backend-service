package edu.stanford.protege.webprotege.change;



import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevisionReverterChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final RevisionNumber revisionNumber;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final ChangeRequestId changeRequestId;


    @Inject
    public RevisionReverterChangeListGenerator(@Nonnull RevisionNumber revisionNumber,
                                               @Nonnull RevisionManager revisionManager,
                                               @Nonnull ChangeRequestId changeRequestId) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.revisionManager = checkNotNull(revisionManager);
        this.changeRequestId = checkNotNull(changeRequestId);
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        Optional<Revision> revision = revisionManager.getRevision(revisionNumber);
        if(revision.isEmpty()) {
            return OntologyChangeList.<Boolean>builder().build(false);
        }
        var changes = new ArrayList<OntologyChange>();
        var theRevision = revision.get();
        for(OntologyChange change : theRevision.getChanges()) {
            var inverseChange = change.getInverseChange();
            changes.add(0, inverseChange);
        }
        return OntologyChangeList.<Boolean>builder().addAll(changes).build(true);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return "Reverted revision " + revisionNumber.getValue();
    }
}
