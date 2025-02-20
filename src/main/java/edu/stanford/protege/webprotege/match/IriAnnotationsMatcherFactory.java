package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.semanticweb.owlapi.model.OWLAnnotation;

public final class IriAnnotationsMatcherFactory {
  private final AnnotationAssertionAxiomsIndex axiomProviderProvider;

  @Inject
  public IriAnnotationsMatcherFactory(
      AnnotationAssertionAxiomsIndex axiomProviderProvider) {
    this.axiomProviderProvider = checkNotNull(axiomProviderProvider, 1);
  }

  public IriAnnotationsMatcher create(Matcher<OWLAnnotation> annotationMatcher) {
    return new IriAnnotationsMatcher(
        checkNotNull(axiomProviderProvider, 1), checkNotNull(annotationMatcher, 2));
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
