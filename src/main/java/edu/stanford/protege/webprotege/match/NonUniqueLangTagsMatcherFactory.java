package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

public final class NonUniqueLangTagsMatcherFactory {
  private final Provider<AnnotationAssertionAxiomsIndex> axiomsIndexProvider;

  @Inject
  public NonUniqueLangTagsMatcherFactory(
      Provider<AnnotationAssertionAxiomsIndex> axiomsIndexProvider) {
    this.axiomsIndexProvider = checkNotNull(axiomsIndexProvider, 1);
  }

  public NonUniqueLangTagsMatcher create(Matcher<OWLAnnotationProperty> propertyMatcher) {
    return new NonUniqueLangTagsMatcher(
        checkNotNull(axiomsIndexProvider.get(), 1), checkNotNull(propertyMatcher, 2));
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
