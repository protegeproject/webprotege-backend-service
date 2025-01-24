package edu.stanford.protege.webprotege.crud.obo;

import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.oboid.OboIdSuffixKit;
import edu.stanford.protege.webprotege.crud.oboid.OboIdSuffixSettings;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class OBOIdSuffixEntityCrudKitPlugin implements EntityCrudKitPlugin<OBOIdSuffixEntityCrudKitHandler, OboIdSuffixSettings, OBOIdSession> {

    @Nonnull
    private final OboIdSuffixKit kit;

    @Nonnull
    private final OBOIdSuffixEntityCrudKitHandlerFactory factory;

    @Inject
    public OBOIdSuffixEntityCrudKitPlugin(@Nonnull OboIdSuffixKit kit,
                                          @Nonnull OBOIdSuffixEntityCrudKitHandlerFactory factory) {
        this.kit = checkNotNull(kit);
        this.factory = checkNotNull(factory);
    }

    @Override
    public EntityCrudKit<OboIdSuffixSettings> getEntityCrudKit() {
        return kit;
    }

    @Override
    public EntityCrudKitHandler<OboIdSuffixSettings, OBOIdSession> getEntityCrudKitHandler() {
        return factory.create(EntityCrudKitPrefixSettings.get(), OboIdSuffixSettings.get(), GeneratedAnnotationsSettings
                .empty());
    }

    @Override
    public EntityCrudKitHandler<OboIdSuffixSettings, OBOIdSession> getEntityCrudKitHandler(EntityCrudKitSettings settings) {
        return factory.create(settings.getPrefixSettings(), (OboIdSuffixSettings) settings.getSuffixSettings(), settings.getGeneratedAnnotationsSettings());
    }

    @Override
    public OboIdSuffixSettings getDefaultSettings() {
        return OboIdSuffixSettings.get();
    }
}
