package edu.stanford.protege.webprotege.crud.obo;

import edu.stanford.protege.webprotege.crud.EntityCrudKitHandler;
import edu.stanford.protege.webprotege.crud.EntityCrudKitPrefixSettings;
import edu.stanford.protege.webprotege.crud.EntityIriPrefixResolver;
import edu.stanford.protege.webprotege.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.protege.webprotege.crud.oboid.OboIdSuffixSettings;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class OBOIdSuffixEntityCrudKitHandlerFactory {

    private final OWLDataFactory dataFactory;

    private final EntitiesInProjectSignatureByIriIndex projectSignatureIndex;

    private final EntityIriPrefixResolver entityIriPrefixResolver;

    public OBOIdSuffixEntityCrudKitHandlerFactory(OWLDataFactory dataFactory,
                                                  EntitiesInProjectSignatureByIriIndex projectSignatureIndex,
                                                  EntityIriPrefixResolver entityIriPrefixResolver) {
        this.dataFactory = dataFactory;
        this.projectSignatureIndex = projectSignatureIndex;
        this.entityIriPrefixResolver = entityIriPrefixResolver;
    }

    public OBOIdSuffixEntityCrudKitHandler create(EntityCrudKitPrefixSettings entityCrudKitPrefixSettings,
                                                  OboIdSuffixSettings oboIdSuffixSettings,
                                                  GeneratedAnnotationsSettings generatedAnnotationsSettings) {
        return new OBOIdSuffixEntityCrudKitHandler(entityCrudKitPrefixSettings,
                                                   oboIdSuffixSettings,
                                                   generatedAnnotationsSettings,
                                                   dataFactory,
                                                   projectSignatureIndex,
                                                   entityIriPrefixResolver);
    }
}
