package edu.stanford.protege.webprotege.owlapi;

import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class RenameMapFactory {

    private final RenderingManager renderingManager;

    private final OWLDataFactory dataFactory;

    @Inject
    public RenameMapFactory(RenderingManager renderingManager, OWLDataFactory dataFactory) {
        this.renderingManager = renderingManager;
        this.dataFactory = dataFactory;
    }

    public RenameMap create(@Nonnull Map<IRI, IRI> renameMap) {
        return new RenameMap(renameMap, dataFactory, renderingManager);
    }
}
