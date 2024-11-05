package edu.stanford.protege.webprotege.forms.json;

import edu.stanford.protege.webprotege.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

public class JsonNameExtractor {

    public static final String JSON_LANG = "json";

    private final ProjectAnnotationAssertionAxiomsBySubjectIndex index;

    public JsonNameExtractor(ProjectAnnotationAssertionAxiomsBySubjectIndex index) {
        this.index = index;
    }

    @Nonnull
    public Optional<String> getJsonName(IRI iri) {
        return index.getAnnotationAssertionAxioms(iri)
                .filter(ax -> ax.getProperty().isLabel())
                .map(OWLAnnotationAssertionAxiom::getValue)
                .filter(OWLAnnotationValue::isLiteral)
                .map(v -> (OWLLiteral) v)
                .filter(l -> l.hasLang(JSON_LANG))
                .map(OWLLiteral::getLiteral)
                .findFirst();
    }
}
