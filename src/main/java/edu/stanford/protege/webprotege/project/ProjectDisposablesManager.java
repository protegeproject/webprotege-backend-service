package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.HasDispose;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Nov 2018
 */
public class ProjectDisposablesManager {

    @Nonnull
    private final DisposableObjectManager disposableObjectManager;

    @Inject
    public ProjectDisposablesManager(@Nonnull DisposableObjectManager disposableObjectManager) {
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
    }

    public void register(@Nonnull HasDispose dispose) {
        disposableObjectManager.register(dispose);
    }

    public void dispose() {
        disposableObjectManager.dispose();
    }
}
