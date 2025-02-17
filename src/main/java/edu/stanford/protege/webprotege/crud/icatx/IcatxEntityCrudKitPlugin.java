package edu.stanford.protege.webprotege.crud.icatx;

import edu.stanford.protege.webprotege.crud.*;

import javax.annotation.Nonnull;

public class IcatxEntityCrudKitPlugin implements EntityCrudKitPlugin<IcatxSuffixEntityCrudKitHandler, IcatxSuffixSettings, ChangeSetEntityCrudSession> {


    private final IcatxGenerationSuffixKit kit;
    @Nonnull
    private final IcatxSuffixEntityCrudKitHandlerFactory factory;


    public IcatxEntityCrudKitPlugin(@Nonnull IcatxGenerationSuffixKit kit,
                                    @Nonnull IcatxSuffixEntityCrudKitHandlerFactory factory) {
        this.kit = kit;
        this.factory = factory;
    }

    @Override
    public EntityCrudKit<IcatxSuffixSettings> getEntityCrudKit() {
        return kit;
    }

    @Override
    public EntityCrudKitHandler<IcatxSuffixSettings, ChangeSetEntityCrudSession> getEntityCrudKitHandler() {
        return factory.create(IcatxSuffixSettings.get(), EntityCrudKitPrefixSettings.get());
    }

    @Override
    public EntityCrudKitHandler<IcatxSuffixSettings, ChangeSetEntityCrudSession> getEntityCrudKitHandler(EntityCrudKitSettings settings) {
        return factory.create((IcatxSuffixSettings) settings.getSuffixSettings(), settings.getPrefixSettings());
    }

    @Override
    public IcatxSuffixSettings getDefaultSettings() {
        return null;
    }
}
