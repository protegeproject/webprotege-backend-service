package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

public final class LeafClassMatcherFactory {
  private final Provider<ClassHierarchyProvider> hierarchyProviderProvider;

  @Inject
  public LeafClassMatcherFactory(Provider<ClassHierarchyProvider> hierarchyProviderProvider) {
    this.hierarchyProviderProvider = checkNotNull(hierarchyProviderProvider, 1);
  }

  public LeafClassMatcher create() {
    return new LeafClassMatcher(checkNotNull(hierarchyProviderProvider.get(), 1));
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
