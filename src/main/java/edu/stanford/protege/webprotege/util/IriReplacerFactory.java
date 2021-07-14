package edu.stanford.protege.webprotege.util;

import com.google.common.collect.ImmutableMap;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class IriReplacerFactory {

    private final OWLDataFactory dataFactory;

    public IriReplacerFactory(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Nonnull
    public IriReplacer create(ImmutableMap<IRI, IRI> iriMap) {
        return new IriReplacer(dataFactory, iriMap);
    }
}
