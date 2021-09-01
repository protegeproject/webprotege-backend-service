package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.criteria.HierarchyFilterType;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLClass;

public final class SubClassOfMatcherFactory {
  private final Provider<ClassHierarchyProvider> providerProvider;

  @Inject
  public SubClassOfMatcherFactory(Provider<ClassHierarchyProvider> providerProvider) {
    this.providerProvider = checkNotNull(providerProvider, 1);
  }

  public SubClassOfMatcher create(OWLClass cls, HierarchyFilterType filterType) {
    return new SubClassOfMatcher(
        checkNotNull(providerProvider.get(), 1), checkNotNull(cls, 2), checkNotNull(filterType, 3));
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
