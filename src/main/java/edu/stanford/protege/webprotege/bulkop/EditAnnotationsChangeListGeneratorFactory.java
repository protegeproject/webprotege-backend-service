package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import java.util.Optional;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class EditAnnotationsChangeListGeneratorFactory {
  private final Provider<OWLDataFactory> dataFactoryProvider;

  private final Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider;

  private final Provider<AnnotationAssertionAxiomsBySubjectIndex> annotationAssertionsIndexProvider;

  @Inject
  public EditAnnotationsChangeListGeneratorFactory(
      Provider<OWLDataFactory> dataFactoryProvider,
      Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider,
      Provider<AnnotationAssertionAxiomsBySubjectIndex> annotationAssertionsIndexProvider) {
    this.dataFactoryProvider = checkNotNull(dataFactoryProvider, 1);
    this.projectOntologiesIndexProvider = checkNotNull(projectOntologiesIndexProvider, 2);
    this.annotationAssertionsIndexProvider = checkNotNull(annotationAssertionsIndexProvider, 3);
  }

  public EditAnnotationsChangeListGenerator create(ChangeRequestId changeRequestId,
                                                   ImmutableSet<OWLEntity> entities,
                                                   Operation operation,
                                                   Optional<OWLAnnotationProperty> matchProperty,
                                                   Optional<String> matchLexicalValue,
                                                   boolean regEx,
                                                   Optional<String> matchLangTag,
                                                   NewAnnotationData newAnnotationData,
                                                   String commitMessage) {
    return new EditAnnotationsChangeListGenerator(changeRequestId,
        checkNotNull(dataFactoryProvider.get(), 1),
        checkNotNull(projectOntologiesIndexProvider.get(), 2),
        checkNotNull(annotationAssertionsIndexProvider.get(), 3),
        checkNotNull(entities, 4),
        checkNotNull(operation, 5),
        checkNotNull(matchProperty, 6),
        checkNotNull(matchLexicalValue, 7),
        regEx,
        checkNotNull(matchLangTag, 9),
        checkNotNull(newAnnotationData, 10),
        checkNotNull(commitMessage, 11));
  }

  private static <T> T checkNotNull(T reference, int argumentIndex) {
    if (reference == null) {
      throw new NullPointerException(
          " method argument is null but is not marked @Nullable. Argument index: "
              + argumentIndex);
    }
    return reference;
  }
}
