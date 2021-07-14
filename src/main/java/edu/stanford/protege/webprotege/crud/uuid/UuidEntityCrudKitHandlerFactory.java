package edu.stanford.protege.webprotege.crud.uuid;

import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityIriPrefixResolver;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class UuidEntityCrudKitHandlerFactory {

    private final OWLDataFactory dataFactory;

    private final EntitiesInProjectSignatureByIriIndex entitiesInSignature;

    private final EntityIriPrefixResolver entityIriPrefixResolover;

    public UuidEntityCrudKitHandlerFactory(OWLDataFactory dataFactory,
                                           EntitiesInProjectSignatureByIriIndex entitiesInSignature,
                                           EntityIriPrefixResolver entityIriPrefixResolover) {
        this.dataFactory = dataFactory;
        this.entitiesInSignature = entitiesInSignature;
        this.entityIriPrefixResolover = entityIriPrefixResolover;
    }

    public UuidEntityCrudKitHandler create(EntityCrudKitPrefixSettings entityCrudKitPrefixSettings,
                                           UuidSuffixSettings uuidSuffixSettings,
                                           GeneratedAnnotationsSettings generatedAnnotationsSettings) {
        return new UuidEntityCrudKitHandler(entityCrudKitPrefixSettings,
                                            uuidSuffixSettings,
                                            generatedAnnotationsSettings,
                                            dataFactory,
                                            entitiesInSignature,
                                            entityIriPrefixResolover);
    }
}
