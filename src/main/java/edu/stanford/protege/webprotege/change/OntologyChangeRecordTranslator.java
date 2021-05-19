package edu.stanford.protege.webprotege.change;

import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
public interface OntologyChangeRecordTranslator {

    @Nonnull
    OntologyChange getOntologyChange(@Nonnull OWLOntologyChangeRecord record);
}
