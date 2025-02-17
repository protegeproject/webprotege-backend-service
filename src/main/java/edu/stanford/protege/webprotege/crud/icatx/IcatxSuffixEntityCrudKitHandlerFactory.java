package edu.stanford.protege.webprotege.crud.icatx;

import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityIriPrefixResolver;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import org.semanticweb.owlapi.model.OWLDataFactory;

public class IcatxSuffixEntityCrudKitHandlerFactory {

    private final EntityIriPrefixResolver entityIriPrefixResolver;
    private final OWLDataFactory owlDataFactory;
    private final CommandExecutor<GetUniqueIdRequest, GetUniqueIdResponse> uniqueIdExecutor;

    public IcatxSuffixEntityCrudKitHandlerFactory(EntityIriPrefixResolver entityIriPrefixResolver, OWLDataFactory owlDataFactory, CommandExecutor<GetUniqueIdRequest, GetUniqueIdResponse> uniqueIdExecutor) {
        this.entityIriPrefixResolver = entityIriPrefixResolver;
        this.owlDataFactory = owlDataFactory;
        this.uniqueIdExecutor = uniqueIdExecutor;
    }


    public IcatxSuffixEntityCrudKitHandler create(IcatxSuffixSettings icatxSuffixSettings,
                                                  EntityCrudKitPrefixSettings entityCrudKitPrefixSetting){
        return new IcatxSuffixEntityCrudKitHandler(icatxSuffixSettings, entityCrudKitPrefixSetting, uniqueIdExecutor, owlDataFactory, this.entityIriPrefixResolver);
    }
}
