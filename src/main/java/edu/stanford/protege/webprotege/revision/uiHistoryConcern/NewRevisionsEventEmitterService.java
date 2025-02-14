package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.revision.Revision;

import java.util.Optional;

public interface NewRevisionsEventEmitterService {
    void emitNewRevisionsEvent(Optional<Revision> revision, ChangeRequestId changeRequestId);
}
