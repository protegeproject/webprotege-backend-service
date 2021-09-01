package edu.stanford.protege.webprotege.frame;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-31
 */
public class PlainFrameRenderer {

    private final FrameComponentRenderer renderer;

    private final Comparator<? super PropertyValue> propertyValueComparator;

    public PlainFrameRenderer(FrameComponentRenderer renderer,
                              Comparator<? super PropertyValue> propertyValueComparator) {
        this.renderer = renderer;
        this.propertyValueComparator = propertyValueComparator;
    }

    public ClassFrame toClassFrame(PlainClassFrame frame) {
        return ClassFrame.get(renderer.getRendering(frame.getSubject()),
                              frame.getParents().stream().map(renderer::getRendering).collect(toImmutableSet()),
                              frame.getPropertyValues().stream().map(this::toPropertyValue).sorted(propertyValueComparator).collect(toImmutableSet()));
    }

    public AnnotationPropertyFrame toAnnotationPropertyFrame(PlainAnnotationPropertyFrame frame) {
        return AnnotationPropertyFrame.get(
                renderer.getRendering(frame.getSubject()),
                frame.getPropertyValues().stream().map(this::toPropertyAnnotationValue).sorted(propertyValueComparator).collect(toImmutableSet()),
                frame.getDomains().stream().flatMap(d -> renderer.getRendering(d).stream()).collect(toImmutableSet()),
                frame.getRanges().stream().flatMap(r -> renderer.getRendering(r).stream()).collect(toImmutableSet())
        );
    }

    @Nonnull
    public DataPropertyFrame toDataPropertyFrame(PlainDataPropertyFrame frame) {
        return DataPropertyFrame.get(
                renderer.getRendering(frame.getSubject()),
                frame.getPropertyValues().stream().map(this::toPropertyValue).sorted(propertyValueComparator).collect(toImmutableSet()),
                frame.getDomains().stream().map(renderer::getRendering).collect(toImmutableSet()),
                frame.getRanges().stream().map(renderer::getRendering).collect(toImmutableSet()),
                frame.isFunctional()
        );
    }

    @Nonnull
    public NamedIndividualFrame toNamedIndividualFrame(PlainNamedIndividualFrame frame) {
        return NamedIndividualFrame.get(
                renderer.getRendering(frame.getSubject()),
                frame.getParents().stream()
                            .map(renderer::getRendering)
                            .collect(toImmutableSet()),
                frame.getPropertyValues().stream().map(this::toPropertyValue)
                                   .sorted(propertyValueComparator)
                                   .collect(toImmutableSet()),
                frame.getSameIndividuals().stream().map(renderer::getRendering).collect(toImmutableSet())
        );
    }

    @Nonnull
    public ObjectPropertyFrame toObjectPropertyFrame(PlainObjectPropertyFrame frame) {
        return ObjectPropertyFrame.get(
                renderer.getRendering(frame.getSubject()),
                frame.getPropertyValues().stream().map(this::toPropertyAnnotationValue).sorted(propertyValueComparator).collect(toImmutableSet()),
                frame.getDomains().stream().map(renderer::getRendering).collect(toImmutableSet()),
                frame.getRanges().stream().map(renderer::getRendering).collect(toImmutableSet()),
                frame.getInverseProperties().stream().map(renderer::getRendering).collect(toImmutableSet()),
                frame.getCharacteristics()
        );
    }


    public PropertyValue toPropertyValue(PlainPropertyValue pv) {
        return pv.accept(new PlainPropertyValueVisitor<PropertyValue>() {
            @Override
            public PropertyValue visit(PlainPropertyClassValue propertyValue) {
                return toPropertyClassValue(propertyValue);
            }

            @Override
            public PropertyValue visit(PlainPropertyAnnotationValue propertyValue) {
                return toPropertyAnnotationValue(propertyValue);
            }

            @Override
            public PropertyValue visit(PlainPropertyDatatypeValue propertyValue) {
                return toPropertyDatatypeValue(propertyValue);
            }

            @Override
            public PropertyValue visit(PlainPropertyIndividualValue propertyValue) {
                return toPropertyIndividualValue(propertyValue);
            }

            @Override
            public PropertyValue visit(PlainPropertyLiteralValue propertyValue) {
                return toPropertyLiteralValue(propertyValue);
            }
        });
    }


    @Nonnull
    public PropertyAnnotationValue toPropertyAnnotationValue(@Nonnull PlainPropertyAnnotationValue pv) {
        return PropertyAnnotationValue.get(
                renderer.getRendering(pv.getProperty()),
                renderer.getRendering(pv.getValue()),
                pv.getState()
        );
    }


    @Nonnull
    public PropertyClassValue toPropertyClassValue(@Nonnull PlainPropertyClassValue pv) {
        return PropertyClassValue.get(
                renderer.getRendering(pv.getProperty()),
                renderer.getRendering(pv.getValue()),
                pv.getState()
        );
    }

    @Nonnull
    public PropertyDatatypeValue toPropertyDatatypeValue(@Nonnull PlainPropertyDatatypeValue pv) {
        return PropertyDatatypeValue.get(
                renderer.getRendering(pv.getProperty()),
                renderer.getRendering(pv.getValue()),
                pv.getState()
        );
    }

    @Nonnull
    public PropertyIndividualValue toPropertyIndividualValue(@Nonnull PlainPropertyIndividualValue pv) {
        return PropertyIndividualValue.get(
                renderer.getRendering(pv.getProperty()),
                renderer.getRendering(pv.getValue()),
                pv.getState()
        );
    }

    @Nonnull
    public PropertyLiteralValue toPropertyLiteralValue(@Nonnull PlainPropertyLiteralValue pv) {
        return PropertyLiteralValue.get(
                renderer.getRendering(pv.getProperty()),
                renderer.getRendering(pv.getValue()),
                pv.getState()
        );
    }

}
