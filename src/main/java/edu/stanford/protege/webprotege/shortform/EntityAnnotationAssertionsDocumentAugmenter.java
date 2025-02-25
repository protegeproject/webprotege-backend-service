package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.common.AnnotationAssertionDictionaryLanguage;
import edu.stanford.protege.webprotege.common.AnnotationAssertionPathDictionaryLanguage;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.protege.webprotege.shortform.EntityDocumentFieldNames.DEPRECATED_FALSE;
import static edu.stanford.protege.webprotege.shortform.EntityDocumentFieldNames.DEPRECATED_TRUE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class EntityAnnotationAssertionsDocumentAugmenter implements EntityDocumentAugmenter {

    @Nonnull
    private final ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final DictionaryLanguageFieldWriter fieldWriter;


    @Inject
    public EntityAnnotationAssertionsDocumentAugmenter(@Nonnull ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                                       @Nonnull DictionaryLanguageFieldWriter fieldWriter) {
        this.annotationAssertionsIndex = annotationAssertionsIndex;
        this.fieldWriter = fieldWriter;
    }

    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        addAnnotationAssertions(entity, document, new HashSet<>());
    }

    private void addAnnotationAssertions(OWLEntity entity, Document document, Set<OWLAnnotationSubject> traversedSubjects) {
        var entityIri = entity.getIRI();
        if(traversedSubjects.contains(entityIri)) {
            return;
        }
        traversedSubjects.add(entityIri);
        var deprecatedAssertions = new ArrayList<OWLAnnotationAssertionAxiom>();
        annotationAssertionsIndex.getAnnotationAssertionAxioms(entityIri)
                                 .forEach(ax -> {
                                     var path = new LinkedHashSet<OWLAnnotationProperty>();
                                     processAnnotationAssertionAxiom(ax, path, document, traversedSubjects);
                                     if(ax.isDeprecatedIRIAssertion()) {
                                         deprecatedAssertions.add(ax);
                                     }
                                 });

        // Special treatment for deprecated to allow search filtering

        var deprecated = !deprecatedAssertions.isEmpty();
        var deprecatedFieldValue = deprecated ? DEPRECATED_TRUE : DEPRECATED_FALSE;
        document.add(new StringField(EntityDocumentFieldNames.DEPRECATED, deprecatedFieldValue, Field.Store.NO));
    }

    /**
     * Process a path to an annotation value that is a literal.
     * @param ax The axiom
     * @param path The current path
     * @throws ClassCastException if the axiom does not have a subject that is a literal
     */
    private void processAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom ax,
                                                 LinkedHashSet<OWLAnnotationProperty> path,
                                                 Document document,
                                                 Set<OWLAnnotationSubject> traversedSubjects) {
        var value = ax.getValue();
        if(value instanceof OWLLiteral) {
            processAnnotationAssertionPathToLiteralTerminal(ax, path, document);
        }
        else if(value instanceof OWLAnnotationSubject) {
            processAnnotationPathToAnnotationSubject(ax, path, document, traversedSubjects);
        }
    }

    /**
     * Process a path to an annotation subject (either an IRI or anonymous individual)
     * @param ax The annotation assertion axiom
     * @param path The current path
     * @param document The document to add fields to
     */
    private void processAnnotationPathToAnnotationSubject(@Nonnull OWLAnnotationAssertionAxiom ax,
                                                          @Nonnull LinkedHashSet<OWLAnnotationProperty> path,
                                                          @Nonnull Document document,
                                                          @Nonnull Set<OWLAnnotationSubject> traversedSubjects) {
        var subject = (OWLAnnotationSubject) ax.getValue();
        if(traversedSubjects.contains(subject)) {
            return;
        }
        traversedSubjects.add(subject);
        if(path.add(ax.getProperty())) {
            annotationAssertionsIndex.getAnnotationAssertionAxioms(subject)
                                     .forEach(nestedAx -> processAnnotationAssertionAxiom(nestedAx, path, document, traversedSubjects));
            path.remove(ax.getProperty());
        }
    }

    private void processAnnotationAssertionPathToLiteralTerminal(OWLAnnotationAssertionAxiom ax,
                                                                 LinkedHashSet<OWLAnnotationProperty> path,
                                                                 Document document) {
        var literal = (OWLLiteral) ax.getValue();
        // Push
        path.add(ax.getProperty());
        var dictionaryLanguage = getDictionaryLanguage(path, literal.getLang());
        // Remove last element
        path.remove(ax.getProperty());

        var lexicalValue = literal.getLiteral();
        fieldWriter.addFieldForDictionaryLanguage(document, dictionaryLanguage, lexicalValue);
    }

    private static DictionaryLanguage getDictionaryLanguage(LinkedHashSet<OWLAnnotationProperty> path,
                                                            String lang) {
        if(path.size() == 1) {
            var annotationPropertyIri = path.stream().findFirst().orElseThrow().getIRI();
            return AnnotationAssertionDictionaryLanguage.get(annotationPropertyIri, lang);
        }
        else {
            var iriPath = path.stream()
                              .map(OWLAnnotationProperty::getIRI)
                              .collect(toImmutableList());
            return AnnotationAssertionPathDictionaryLanguage.get(iriPath, lang);
        }
    }
}
