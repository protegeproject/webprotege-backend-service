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
public class ApplicationSchemeSupplier implements Supplier<ApplicationScheme> {

    @Nonnull
    private final ApplicationPreferencesStore manager;

    @Inject
    public ApplicationSchemeSupplier(@Nonnull ApplicationPreferencesStore manager) {
        this.manager = checkNotNull(manager);
    }

    @Nonnull
    public ApplicationScheme get() {
        return ApplicationScheme.valueOf(manager.getApplicationPreferences().getApplicationLocation().getScheme().toUpperCase());
    }
}
