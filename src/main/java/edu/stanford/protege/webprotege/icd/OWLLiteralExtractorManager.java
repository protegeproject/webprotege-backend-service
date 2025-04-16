package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.*;

public class OWLLiteralExtractorManager {

    private final AnnotationAssertionAxiomsIndex annotationAssertionAxiomsIndex;

    @Inject
    public OWLLiteralExtractorManager(AnnotationAssertionAxiomsIndex annotationAssertionAxiomsIndex) {
        this.annotationAssertionAxiomsIndex = annotationAssertionAxiomsIndex;
    }

    public OWLLiteral getOwlLiteralValueFromEntity(OWLEntity entity, IcdProperties... propertyPath) {
        Optional<OWLAnnotationValue> annotationValue = Optional.of(entity.getIRI());

        for (IcdProperties property : propertyPath) {
            annotationValue = annotationValue.flatMap(value -> {
                if (value instanceof IRI iri) {
                    return annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(iri)
                            .filter(ax -> Objects.equals(property.getValue(), ax.getProperty().getIRI().toString()))
                            .map(OWLAnnotationAssertionAxiom::getValue)
                            .findFirst();
                }
                return Optional.empty();
            });
        }

        return annotationValue
                .filter(OWLAnnotationValue::isLiteral)
                .map(val -> (OWLLiteral) val)
                .orElse(DataFactory.getOWLLiteral(""));
    }
}
