package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.renderer.ContextRenderer;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-15
 */
public class EmptyEntityFrameFactory {

    @Nonnull
    private final ContextRenderer renderingManager;

    @Inject
    public EmptyEntityFrameFactory(@Nonnull ContextRenderer renderingManager) {
        this.renderingManager = checkNotNull(renderingManager);
    }

    public PlainEntityFrame getEmptyEntityFrame(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorEx<PlainEntityFrame>() {
            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLClass cls) {
                return PlainClassFrame.empty(cls);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLObjectProperty property) {
                return PlainObjectPropertyFrame.empty(property);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLDataProperty property) {
                return PlainDataPropertyFrame.empty(property);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLNamedIndividual individual) {
                return PlainNamedIndividualFrame.empty(individual);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLDatatype datatype) {
                throw new UnsupportedOperationException();
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLAnnotationProperty property) {
                return PlainAnnotationPropertyFrame.empty(property);
            }
        });
    }
}
