package edu.stanford.protege.webprotege.crud.supplied;

import edu.stanford.protege.webprotege.crud.ChangeSetEntityCrudSession;
import edu.stanford.protege.webprotege.crud.EntityCrudKitHandler;
import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityIriPrefixResolver;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class SuppliedNameSuffixEntityCrudKitHandlerFactory {

    private final OWLDataFactory dataFactory;

    private final EntityIriPrefixResolver entityIriPrefixResolver;

    public SuppliedNameSuffixEntityCrudKitHandlerFactory(OWLDataFactory dataFactory,
                                                         EntityIriPrefixResolver entityIriPrefixResolver) {
        this.dataFactory = dataFactory;
        this.entityIriPrefixResolver = entityIriPrefixResolver;
    }

    public SuppliedNameSuffixEntityCrudKitHandler create(EntityCrudKitPrefixSettings entityCrudKitPrefixSettings,
                                                                SuppliedNameSuffixSettings suppliedNameSuffixSettings,
                                                                GeneratedAnnotationsSettings generatedAnnotationsSettings) {
        return new SuppliedNameSuffixEntityCrudKitHandler(entityCrudKitPrefixSettings,
                                                          suppliedNameSuffixSettings,
                                                          generatedAnnotationsSettings,
                                                          dataFactory,
                                                          entityIriPrefixResolver);
    }
}
