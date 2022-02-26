package edu.stanford.protege.webprotege.dispatch.actions;

import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-02-25
 */
public interface AxiomsSource {

    @Nonnull
    Stream<OWLAxiom> getAxioms();
}
