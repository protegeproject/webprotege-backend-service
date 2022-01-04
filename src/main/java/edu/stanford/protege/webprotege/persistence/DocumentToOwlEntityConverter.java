package edu.stanford.protege.webprotege.persistence;

import org.bson.Document;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.core.convert.converter.Converter;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-01-04
 */
public class DocumentToOwlEntityConverter implements Converter<Document, OWLEntity> {

    private final OWLDataFactory dataFactory;

    public DocumentToOwlEntityConverter(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLEntity convert(Document document) {
        var iriString = document.getString("iri");
        var entityTypeString = document.getString("type");
        for (EntityType entityType : EntityType.values()) {
            if (entityTypeString.equals(entityType.toString())) {
                return dataFactory.getOWLEntity(entityType, IRI.create(iriString));
            }
        }
        return null;
    }
}
