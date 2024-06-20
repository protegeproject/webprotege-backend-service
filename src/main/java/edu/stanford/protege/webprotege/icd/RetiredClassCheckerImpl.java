package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class RetiredClassCheckerImpl implements RetiredClassChecker {

    private final AnnotationAssertionAxiomsIndex annotationAssertionAxiomsIndex;


    @Inject
    public RetiredClassCheckerImpl(AnnotationAssertionAxiomsIndex annotationAssertionAxiomsIndex) {
        this.annotationAssertionAxiomsIndex = checkNotNull(annotationAssertionAxiomsIndex);
    }

    @Override
    public boolean isRetired(@NotNull OWLEntity entity) {

        String titleValue = getIcdTitleValueForEntity(entity);

        return titleValue.contains(IcdConstants.RETIRED_TITLE_PART);
    }

    private String getIcdTitleValueForEntity(OWLEntity entity) {

        OWLLiteral titleLiteral = getOwlLiteralWithTitleValueFromEntity(entity);

        var titleValue = titleLiteral.getLiteral();

        return titleValue;
    }

    private OWLLiteral getOwlLiteralWithTitleValueFromEntity(OWLEntity entity) {
        var titleAndLabelAnnotations = annotationAssertionAxiomsIndex.getAnnotationAssertionAxioms(entity.getIRI())
                .filter(ax -> ax.getProperty().getIRI().toString().equals(IcdConstants.TITLE_PROP))
                .map(OWLAnnotationAssertionAxiom::getValue)
                .filter(OWLObject::isIRI)
                .map(val -> (IRI) val)
                .flatMap(annotationAssertionAxiomsIndex::getAnnotationAssertionAxioms)
                .filter(ax2 -> ax2.getProperty().getIRI().toString().equals(IcdConstants.LABEL_PROP))
                .map(OWLAnnotationAssertionAxiom::getValue)
                .filter(OWLAnnotationValue::isLiteral)
                .map(val2 -> (OWLLiteral) val2)
                .findFirst();

        return titleAndLabelAnnotations.orElse(DataFactory.getOWLLiteral(""));
    }
}
