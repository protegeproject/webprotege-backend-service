package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class AnnotationValuesAreNotDisjointMatcherFactory {
  private final Provider<AnnotationAssertionAxiomsIndex> axiomsProvider;

  @Inject
  public AnnotationValuesAreNotDisjointMatcherFactory(
      Provider<AnnotationAssertionAxiomsIndex> axiomsProvider) {
    this.axiomsProvider = checkNotNull(axiomsProvider, 1);
  }

  public AnnotationValuesAreNotDisjointMatcher create(
      Matcher<OWLAnnotationProperty> propertyA, Matcher<OWLAnnotationProperty> propertyB) {
    return new AnnotationValuesAreNotDisjointMatcher(
        checkNotNull(axiomsProvider.get(), 1),
        checkNotNull(propertyA, 2),
        checkNotNull(propertyB, 3));
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
