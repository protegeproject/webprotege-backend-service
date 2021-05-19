package edu.stanford.protege.webprotege.change.description;

import edu.stanford.protege.webprotege.owlapi.OWLObjectStringFormatter;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
public interface StructuredChangeDescription {

    @Nonnull
    String getTypeName();

    @Nonnull
    String formatDescription(@Nonnull OWLObjectStringFormatter formatter);
}
