package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.project.Ontology;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;

import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class OntologyDiffCalculator {


    private final AnnotationDiffCalculator annotationDiffCalculator;

    private final AxiomDiffCalculator axiomDiffCalculator;

    @Inject
    public OntologyDiffCalculator(AnnotationDiffCalculator annotationDiffCalculator, AxiomDiffCalculator axiomDiffCalculator) {
        this.annotationDiffCalculator = annotationDiffCalculator;
        this.axiomDiffCalculator = axiomDiffCalculator;
    }

    public OntologyDiff computeDiff(Ontology from, Ontology to) {
        Diff<OWLAxiom> axiomDiff = axiomDiffCalculator.computeDiff(from, to);
        Diff<OWLAnnotation> annotationDiff = annotationDiffCalculator.computeDiff(from, to);
        return new OntologyDiff(from.getOntologyId(), to.getOntologyId(), annotationDiff, axiomDiff);
    }
}
