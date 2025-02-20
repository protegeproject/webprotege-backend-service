package edu.stanford.protege.webprotege.axiom;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;

import jakarta.inject.Inject;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class AxiomByRenderingComparator implements Comparator<OWLAxiom> {

    private final OWLObjectRenderer renderer;

    @Inject
    public AxiomByRenderingComparator(OWLObjectRenderer renderer) {
        this.renderer = checkNotNull(renderer);
    }

    @Override
    public int compare(OWLAxiom o1, OWLAxiom o2) {
        String rendering1 = renderer.render(o1);
        String rendering2 = renderer.render(o2);
        return rendering1.compareToIgnoreCase(rendering2);
    }
}
