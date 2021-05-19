package edu.stanford.protege.webprotege.crud.uuid;

import edu.stanford.protege.webprotege.crud.ChangeSetEntityCrudSession;
import edu.stanford.protege.webprotege.crud.EntityCrudKitPlugin;
import edu.stanford.protege.webprotege.crud.EntityCrudKit;
import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSettings;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class UuidEntityCrudKitPlugin implements EntityCrudKitPlugin<UuidEntityCrudKitHandler, UuidSuffixSettings, ChangeSetEntityCrudSession> {

    @Nonnull
    private UuidSuffixKit kit;

    @Nonnull
    private final UuidEntityCrudKitHandlerFactory factory;

    @Inject
    public UuidEntityCrudKitPlugin(@Nonnull UuidSuffixKit kit,
                                   @Nonnull UuidEntityCrudKitHandlerFactory factory) {
        this.kit = checkNotNull(kit);
        this.factory = checkNotNull(factory);
    }

    @Override
    public EntityCrudKit<UuidSuffixSettings> getEntityCrudKit() {
        return kit;
    }

    @Override
    public UuidEntityCrudKitHandler getEntityCrudKitHandler() {
        return factory.create(EntityCrudKitPrefixSettings.get(), UuidSuffixSettings.get(), GeneratedAnnotationsSettings.empty());
    }

    @Override
    public UuidSuffixSettings getDefaultSettings() {
        return UuidSuffixSettings.get();
    }

    @Override
    public UuidEntityCrudKitHandler getEntityCrudKitHandler(EntityCrudKitSettings<UuidSuffixSettings> settings) {
        return factory.create(settings.getPrefixSettings(), settings.getSuffixSettings(), settings.getGeneratedAnnotationsSettings());
    }
}

