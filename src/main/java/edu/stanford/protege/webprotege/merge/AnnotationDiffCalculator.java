package edu.stanford.protege.webprotege.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.project.Ontology;
import org.semanticweb.owlapi.model.OWLAnnotation;

import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class AnnotationDiffCalculator {

    @Inject
    public AnnotationDiffCalculator() {
    }

    public Diff<OWLAnnotation> computeDiff(Ontology from, Ontology to) {
        ImmutableSet.Builder<OWLAnnotation> addedAnnotations = ImmutableSet.builder();
        ImmutableSet.Builder<OWLAnnotation> removedAnnotations = ImmutableSet.builder();
        for(OWLAnnotation anno : to.getAnnotations()) {
            if(!from.getAnnotations().contains(anno)) {
                addedAnnotations.add(anno);
            }
        }
        for(OWLAnnotation anno : from.getAnnotations()) {
            if(!to.getAnnotations().contains(anno)) {
                removedAnnotations.add(anno);
            }
        }
        return new Diff<>(addedAnnotations.build(), removedAnnotations.build());
    }
}
