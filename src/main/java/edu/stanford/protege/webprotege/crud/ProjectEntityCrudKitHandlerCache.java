package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.protege.webprotege.crud.uuid.UuidSuffixSettings;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
@ProjectSingleton
public class ProjectEntityCrudKitHandlerCache {

    private final ProjectId projectId;

    private final ProjectEntityCrudKitSettingsRepository repository;

    private final EntityCrudKitRegistry registry;

    private EntityCrudKitHandler<?, ?> cachedHandler;

    @Inject
    public ProjectEntityCrudKitHandlerCache(@Nonnull ProjectEntityCrudKitSettingsRepository repository,
                                            @Nonnull ProjectId projectId,
                                            @Nonnull EntityCrudKitRegistry registry) {
        this.repository = checkNotNull(repository);
        this.projectId = checkNotNull(projectId);
        this.registry = checkNotNull(registry);
    }

    /**
     * Gets the current {@link EntityCrudKitHandler}.
     * @return The current {@link EntityCrudKitHandler}.  Not {@code null}.
     */
    public synchronized EntityCrudKitHandler<?, ?> getHandler() {
        EntityCrudKitSettings settings = getCurrentSettings();
        if (isCachedHandlerStale(settings)) {
            cachedHandler = registry.getHandler(settings);
        }
        return cachedHandler;
    }

    private EntityCrudKitSettings getCurrentSettings() {
        Optional<ProjectEntityCrudKitSettings> settings = repository.findOne(projectId);
        ProjectEntityCrudKitSettings projectSettings;
        if (!settings.isPresent()) {
            projectSettings = ProjectEntityCrudKitSettings.get(projectId, getDefaultSettings());
            repository.save(projectSettings);
        }
        else {
            projectSettings = settings.get();
        }
        return projectSettings.getSettings();
    }

    private boolean isCachedHandlerStale(EntityCrudKitSettings settings) {
        return cachedHandler == null || !settings.equals(cachedHandler.getSettings());
    }

    /**
     * Creates default settings, with default prefix and suffix settings.
     *
     * @return The default settings.  Not {@code null}.
     */
    private static EntityCrudKitSettings getDefaultSettings() {
        return EntityCrudKitSettings.get(getDefaultPrefixSettings(), getDefaultSuffixSettings(), getDefaultGeneratedAnnotationsSettings());
    }

    private static EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return EntityCrudKitPrefixSettings.get();
    }

    private static EntityCrudKitSuffixSettings getDefaultSuffixSettings() {
        return UuidSuffixSettings.get();
    }

    private static GeneratedAnnotationsSettings getDefaultGeneratedAnnotationsSettings() {
        return GeneratedAnnotationsSettings.empty();
    }
}
