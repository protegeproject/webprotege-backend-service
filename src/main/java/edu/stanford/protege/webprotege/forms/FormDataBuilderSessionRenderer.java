package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.frame.FrameComponentSessionRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

@FormDataBuilderSession
public class FormDataBuilderSessionRenderer {

    @Nonnull
    private final FrameComponentSessionRenderer frameComponentSessionRenderer;

    @Inject
    public FormDataBuilderSessionRenderer(@Nonnull FrameComponentSessionRenderer frameComponentSessionRenderer) {
        this.frameComponentSessionRenderer = frameComponentSessionRenderer;
    }

    @Nonnull
    public OWLEntityData getEntityRendering(OWLEntity subject) {
        return frameComponentSessionRenderer.getEntityRendering(subject);
    }

    @Nonnull
    public Collection<OWLEntityData> getRendering(IRI iri) {
        return frameComponentSessionRenderer.getRendering(iri);
    }
}
