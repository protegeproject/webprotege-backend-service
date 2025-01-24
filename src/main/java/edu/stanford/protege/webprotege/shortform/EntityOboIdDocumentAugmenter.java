package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.common.OboIdDictionaryLanguage;
import org.apache.lucene.document.Document;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class EntityOboIdDocumentAugmenter implements EntityDocumentAugmenter {

//    @Nonnull
//    private final DictionaryLanguageFieldWriter fieldWriter;
//
//    @Inject
//    public EntityOboIdDocumentAugmenter(@Nonnull DictionaryLanguageFieldWriter fieldWriter) {
//        this.fieldWriter = fieldWriter;
//    }
//
//    @Override
//    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
//        // Obo Id
//        var entityIri = entity.getIRI();
//        var oboId = OboId.getOboId(entityIri);
//        oboId.ifPresent(s -> fieldWriter.addFieldForDictionaryLanguage(document, OboIdDictionaryLanguage.get(), s));
//    }


    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {

    }
}


