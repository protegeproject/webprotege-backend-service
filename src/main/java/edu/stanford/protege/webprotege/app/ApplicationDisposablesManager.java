package edu.stanford.protege.webprotege.app;

import edu.stanford.protege.webprotege.HasDispose;
import edu.stanford.protege.webprotege.inject.ApplicationSingleton;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;

import javax.annotation.Nonnull;
import javax.annotation.PreDestroy;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Nov 2018
 */
@ApplicationSingleton
public class ApplicationDisposablesManager {

    @Nonnull
    private final DisposableObjectManager disposableObjectManager;

    @Inject
    public ApplicationDisposablesManager(@Nonnull DisposableObjectManager disposableObjectManager) {
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
    }

    public void register(@Nonnull HasDispose disposable) {
        disposableObjectManager.register(disposable);
    }

    @PreDestroy
    public void dispose() {
        disposableObjectManager.dispose();
    }

}
