package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.frame.translator.AnnotationPropertyFrameTranslator;
import edu.stanford.protege.webprotege.frame.translator.DataPropertyFrameTranslator;
import edu.stanford.protege.webprotege.frame.translator.NamedIndividualFrameTranslator;
import edu.stanford.protege.webprotege.frame.translator.ObjectPropertyFrameTranslator;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-06
 */
public class EntityFrameProvider {

    @Nonnull
    private final ClassFrameProvider classFrameProvider;

    @Nonnull
    private final ObjectPropertyFrameTranslator objectPropertyFrameTranslator;

    @Nonnull
    private final DataPropertyFrameTranslator dataPropertyFrameTranslator;

    @Nonnull
    private final AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator;

    @Nonnull
    private final NamedIndividualFrameTranslator namedIndividualFrameTranslator;

    @Inject
    public EntityFrameProvider(@Nonnull ClassFrameProvider classFrameProvider,
                               @Nonnull ObjectPropertyFrameTranslator objectPropertyFrameTranslator,
                               @Nonnull DataPropertyFrameTranslator dataPropertyFrameTranslator,
                               @Nonnull AnnotationPropertyFrameTranslator annotationPropertyFrameTranslator,
                               @Nonnull NamedIndividualFrameTranslator namedIndividualFrameTranslator) {
        this.classFrameProvider = classFrameProvider;
        this.objectPropertyFrameTranslator = objectPropertyFrameTranslator;
        this.dataPropertyFrameTranslator = dataPropertyFrameTranslator;
        this.annotationPropertyFrameTranslator = annotationPropertyFrameTranslator;
        this.namedIndividualFrameTranslator = namedIndividualFrameTranslator;
    }

    @Nonnull
    public PlainEntityFrame getEntityFrame(@Nonnull OWLEntity entity,
                                           boolean includeDerivedInfo) {
        return entity.accept(new OWLEntityVisitorEx<>() {
            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLClass cls) {
                var options = ClassFrameTranslationOptions.get(
                        ClassFrameTranslationOptions.AncestorsTreatment.EXCLUDE_ANCESTORS,
                        RelationshipTranslationOptions.get(
                                RelationshipTranslationOptions.allOutgoingRelationships(),
                                RelationshipTranslationOptions.noIncomingRelationships(),
                                RelationshipTranslationOptions.RelationshipMinification.NON_MINIMIZED_RELATIONSHIPS
                        )
                );
                return classFrameProvider.getFrame(cls, options);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLObjectProperty property) {
                return objectPropertyFrameTranslator.getFrame(property);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLDataProperty property) {
                return dataPropertyFrameTranslator.getFrame(property);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLNamedIndividual individual) {
                return namedIndividualFrameTranslator.getFrame(individual, includeDerivedInfo);
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLDatatype datatype) {
                throw new RuntimeException("Datatypes are not supported");
            }

            @Nonnull
            @Override
            public PlainEntityFrame visit(@Nonnull OWLAnnotationProperty property) {
                return annotationPropertyFrameTranslator.getFrame(property);
            }
        });
    }
}
