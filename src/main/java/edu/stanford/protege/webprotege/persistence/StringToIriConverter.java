package edu.stanford.protege.webprotege.persistence;

import org.semanticweb.owlapi.model.IRI;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-01-04
 */
public class StringToIriConverter implements Converter<String, IRI> {

    @Override
    public IRI convert(String s) {
        return IRI.create(s);
    }
}
