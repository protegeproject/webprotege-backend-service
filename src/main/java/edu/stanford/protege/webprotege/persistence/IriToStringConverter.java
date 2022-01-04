package edu.stanford.protege.webprotege.persistence;

import org.semanticweb.owlapi.model.IRI;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-01-04
 */
public class IriToStringConverter implements Converter<IRI, String> {

    @Override
    public String convert(IRI iri) {
        return iri.toString();
    }
}
