package edu.stanford.bmir.protege.web.server.revision;

import java.util.Optional;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public interface HasGetRevisionSummary {

    Optional<RevisionSummary> getRevisionSummary(RevisionNumber revisionNumber);
}
