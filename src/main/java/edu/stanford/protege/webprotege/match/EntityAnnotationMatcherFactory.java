package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.AnnotationPresence;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLAnnotation;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class EntityAnnotationMatcherFactory {
  private final Provider<AnnotationAssertionAxiomsIndex> axiomProviderProvider;

  @Inject
  public EntityAnnotationMatcherFactory(
      Provider<AnnotationAssertionAxiomsIndex> axiomProviderProvider) {
    this.axiomProviderProvider = checkNotNull(axiomProviderProvider, 1);
  }

  public EntityAnnotationMatcher create(
      Matcher<OWLAnnotation> annotationMatcher, AnnotationPresence annotationPresence) {
    return new EntityAnnotationMatcher(
        checkNotNull(axiomProviderProvider.get(), 1),
        checkNotNull(annotationMatcher, 2),
        checkNotNull(annotationPresence, 3));
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
