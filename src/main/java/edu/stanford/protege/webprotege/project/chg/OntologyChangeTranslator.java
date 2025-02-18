package edu.stanford.protege.webprotege.project.chg;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 */
public class OntologyChangeTranslator {

    @Nonnull
    private final OntologyChangeTranslatorVisitor visitor;

    @Inject
    public OntologyChangeTranslator(@Nonnull OntologyChangeTranslatorVisitor visitor) {
        this.visitor = checkNotNull(visitor);
    }

    @Nonnull
    public OWLOntologyChange toOwlOntologyChange(@Nonnull OntologyChange change) {
        return change.accept(visitor);
    }
}
