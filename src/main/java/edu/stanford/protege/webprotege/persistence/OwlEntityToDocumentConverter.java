package edu.stanford.protege.webprotege.persistence;

import org.bson.Document;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-01-04
 */
public class OwlEntityToDocumentConverter implements Converter<OWLEntity, Document> {

    @Override
    public Document convert(OWLEntity entity) {
        return new Document().append("iri", entity.getIRI().toString())
                             .append("type", entity.getEntityType().toString());
    }
}
