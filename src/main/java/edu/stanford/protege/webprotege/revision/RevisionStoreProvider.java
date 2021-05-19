package edu.stanford.protege.webprotege.revision;

import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
@ProjectSingleton
public class RevisionStoreProvider {

    @Nonnull
    private final RevisionStoreImpl revisionStore;

    @Nonnull
    private final ProjectDisposablesManager disposablesManager;

    private boolean loaded = false;

    @Inject
    public RevisionStoreProvider(@Nonnull RevisionStoreImpl revisionStore,
                                 @Nonnull ProjectDisposablesManager disposablesManager) {
        this.revisionStore = checkNotNull(revisionStore);
        this.disposablesManager = checkNotNull(disposablesManager);
    }

    public synchronized RevisionStore get() {
        if(!loaded) {
            revisionStore.load();
            loaded = true;
            disposablesManager.register(revisionStore);
        }
        return revisionStore;
    }
}
