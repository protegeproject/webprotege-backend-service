package edu.stanford.protege.webprotege.crud.icatx;

import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityIriPrefixResolver;
import org.semanticweb.owlapi.model.OWLDataFactory;

public class IcatxSuffixEntityCrudKitHandlerFactory {

    private final EntityIriPrefixResolver entityIriPrefixResolver;
    private final OWLDataFactory owlDataFactory;

    public IcatxSuffixEntityCrudKitHandlerFactory(EntityIriPrefixResolver entityIriPrefixResolver, OWLDataFactory owlDataFactory) {
        this.entityIriPrefixResolver = entityIriPrefixResolver;
        this.owlDataFactory = owlDataFactory;
    }


    public IcatxSuffixEntityCrudKitHandler create(IcatxSuffixSettings icatxSuffixSettings,
                                                  EntityCrudKitPrefixSettings entityCrudKitPrefixSetting){
        return new IcatxSuffixEntityCrudKitHandler(icatxSuffixSettings, entityCrudKitPrefixSetting, owlDataFactory, this.entityIriPrefixResolver);
    }
}
