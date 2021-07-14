package edu.stanford.protege.webprotege.usage;

import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ReferencingAxiomVisitorFactory {

    private final RenderingManager renderingManager;

    private final EntitiesInProjectSignatureByIriIndex entityInSignatureIndex;

    public ReferencingAxiomVisitorFactory(RenderingManager renderingManager,
                                          EntitiesInProjectSignatureByIriIndex entityInSignatureIndex) {
        this.renderingManager = renderingManager;
        this.entityInSignatureIndex = entityInSignatureIndex;
    }

    public ReferencingAxiomVisitor create(OWLEntity subject) {
        return new ReferencingAxiomVisitor(subject,
                                           renderingManager,
                                           entityInSignatureIndex);
    }
}
