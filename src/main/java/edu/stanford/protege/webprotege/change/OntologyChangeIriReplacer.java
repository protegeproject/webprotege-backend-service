package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.util.IriReplacer;
import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-29
 */
public class OntologyChangeIriReplacer {

    public OntologyChange replaceIris(OntologyChange change, IriReplacer replacer) {
        return change.accept(new Replacer(replacer));
    }


    private static class Replacer implements OntologyChangeVisitorEx<OntologyChange> {

        private final IriReplacer iriReplacer;

        public Replacer(IriReplacer iriReplacer) {
            this.iriReplacer = iriReplacer;
        }

        @Override
        public OntologyChange visit(@Nonnull AddAxiomChange addAxiomChange) {
            return AddAxiomChange.of(addAxiomChange.ontologyId(),
                                     iriReplacer.replaceIris(addAxiomChange.axiom()));
        }

        @Override
        public OntologyChange visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
            return RemoveAxiomChange.of(removeAxiomChange.ontologyId(),
                                     iriReplacer.replaceIris(removeAxiomChange.axiom()));
        }

        @Override
        public OntologyChange visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {
            return AddOntologyAnnotationChange.of(addOntologyAnnotationChange.ontologyId(),
                                                  iriReplacer.replaceIris(addOntologyAnnotationChange.annotation()));
        }

        @Override
        public OntologyChange visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {
            return RemoveOntologyAnnotationChange.of(removeOntologyAnnotationChange.ontologyId(),
                                                  iriReplacer.replaceIris(removeOntologyAnnotationChange.annotation()));
        }

        @Override
        public OntologyChange visit(@Nonnull AddImportChange addImportChange) {
            return addImportChange;
        }

        @Override
        public OntologyChange visit(@Nonnull RemoveImportChange removeImportChange) {
            return removeImportChange;
        }
    }
}
