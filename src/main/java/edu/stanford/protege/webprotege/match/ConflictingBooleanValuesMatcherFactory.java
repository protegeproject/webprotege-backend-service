package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsIndex;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class ConflictingBooleanValuesMatcherFactory {
  private final Provider<AnnotationAssertionAxiomsIndex> axiomsProvider;

  @Inject
  public ConflictingBooleanValuesMatcherFactory(
      Provider<AnnotationAssertionAxiomsIndex> axiomsProvider) {
    this.axiomsProvider = checkNotNull(axiomsProvider, 1);
  }

  public ConflictingBooleanValuesMatcher create() {
    return new ConflictingBooleanValuesMatcher(checkNotNull(axiomsProvider.get(), 1));
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
