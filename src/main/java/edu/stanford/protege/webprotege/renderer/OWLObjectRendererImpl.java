package edu.stanford.protege.webprotege.renderer;

import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.ShortFormProvider;

import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class OWLObjectRendererImpl implements OWLObjectRenderer {

    private final RenderingManager renderingManager;

    @Inject
    public OWLObjectRendererImpl(RenderingManager renderingManager) {
        this.renderingManager = renderingManager;
    }

    @Override
    public void setShortFormProvider(ShortFormProvider shortFormProvider) {

    }

    @Override
    public String render(OWLObject object) {
        return renderingManager.getBrowserText(object);
    }
}
