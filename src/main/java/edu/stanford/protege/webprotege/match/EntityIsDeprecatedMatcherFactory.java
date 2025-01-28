package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class EntityIsDeprecatedMatcherFactory {
  private final AnnotationAssertionAxiomsIndex axiomsProvider;

  @Inject
  public EntityIsDeprecatedMatcherFactory(AnnotationAssertionAxiomsIndex axiomsProvider) {
    this.axiomsProvider = checkNotNull(axiomsProvider, 1);
  }

  public EntityIsDeprecatedMatcher create() {
    return new EntityIsDeprecatedMatcher(checkNotNull(axiomsProvider, 1));
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
