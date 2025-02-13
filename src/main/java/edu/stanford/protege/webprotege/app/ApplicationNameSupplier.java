package edu.stanford.protege.webprotege.app;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationNameSupplier implements Supplier<String> {

    private final ApplicationPreferencesStore manager;

    @Inject
    public ApplicationNameSupplier(@Nonnull ApplicationPreferencesStore manager) {
        this.manager = checkNotNull(manager);
    }

    public String get() {
        return manager.getApplicationPreferences().getApplicationName();
    }
}
