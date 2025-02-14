package edu.stanford.protege.webprotege.app;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.function.Supplier;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationHostSupplier implements Supplier<String> {

    private final ApplicationPreferencesStore applicationPreferencesStore;

    @Inject
    public ApplicationHostSupplier(@Nonnull ApplicationPreferencesStore applicationPreferencesStore) {
        this.applicationPreferencesStore = applicationPreferencesStore;
    }

    @Nonnull
    public String get() {
        return applicationPreferencesStore.getApplicationPreferences().getApplicationLocation().getHost();
    }
}
