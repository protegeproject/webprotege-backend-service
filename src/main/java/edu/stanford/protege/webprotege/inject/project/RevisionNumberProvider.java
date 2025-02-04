package edu.stanford.protege.webprotege.inject.project;

import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class RevisionNumberProvider implements Provider<RevisionNumber> {

    private final RevisionManager revisionManager;

    @Inject
    public RevisionNumberProvider(RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }

    @Override
    public RevisionNumber get() {
        return revisionManager.getCurrentRevision();
    }
}
