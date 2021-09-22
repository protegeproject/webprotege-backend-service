package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.util.IriReplacer;
import org.jetbrains.annotations.NotNull;

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
        public OntologyChange visit(@NotNull AddAxiomChange addAxiomChange) {
            return AddAxiomChange.of(addAxiomChange.ontologyId(),
                                     iriReplacer.replaceIris(addAxiomChange.axiom()));
        }

        @Override
        public OntologyChange visit(@NotNull RemoveAxiomChange removeAxiomChange) {
            return RemoveAxiomChange.of(removeAxiomChange.ontologyId(),
                                     iriReplacer.replaceIris(removeAxiomChange.axiom()));
        }

        @Override
        public OntologyChange visit(@NotNull AddOntologyAnnotationChange addOntologyAnnotationChange) {
            return AddOntologyAnnotationChange.of(addOntologyAnnotationChange.ontologyId(),
                                                  iriReplacer.replaceIris(addOntologyAnnotationChange.annotation()));
        }

        @Override
        public OntologyChange visit(@NotNull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {
            return RemoveOntologyAnnotationChange.of(removeOntologyAnnotationChange.ontologyId(),
                                                  iriReplacer.replaceIris(removeOntologyAnnotationChange.annotation()));
        }

        @Override
        public OntologyChange visit(@NotNull AddImportChange addImportChange) {
            return addImportChange;
        }

        @Override
        public OntologyChange visit(@NotNull RemoveImportChange removeImportChange) {
            return removeImportChange;
        }
    }
}
