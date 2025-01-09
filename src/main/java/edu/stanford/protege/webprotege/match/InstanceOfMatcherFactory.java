package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.ProjectSignatureByTypeIndex;
import edu.stanford.protege.webprotege.criteria.HierarchyFilterType;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.semanticweb.owlapi.model.OWLClass;

public final class InstanceOfMatcherFactory {
  private final ClassHierarchyProvider hierarchyProviderProvider;

  private final ProjectOntologiesIndex projectOntologiesIndexProvider;

  private final ClassAssertionAxiomsByClassIndex classAssertionsByClassProvider;

  private final ProjectSignatureByTypeIndex projectSignatureByTypeProvider;

  @Inject
  public InstanceOfMatcherFactory(
      ClassHierarchyProvider hierarchyProviderProvider,
      ProjectOntologiesIndex projectOntologiesIndexProvider,
      ClassAssertionAxiomsByClassIndex classAssertionsByClassProvider,
      ProjectSignatureByTypeIndex projectSignatureByTypeProvider) {
    this.hierarchyProviderProvider = checkNotNull(hierarchyProviderProvider, 1);
    this.projectOntologiesIndexProvider = checkNotNull(projectOntologiesIndexProvider, 2);
    this.classAssertionsByClassProvider = checkNotNull(classAssertionsByClassProvider, 3);
    this.projectSignatureByTypeProvider = checkNotNull(projectSignatureByTypeProvider, 4);
  }

  public InstanceOfMatcher create(OWLClass target, HierarchyFilterType filterType) {
    return new InstanceOfMatcher(
        checkNotNull(hierarchyProviderProvider, 1),
        checkNotNull(projectOntologiesIndexProvider, 2),
        checkNotNull(classAssertionsByClassProvider, 3),
        checkNotNull(projectSignatureByTypeProvider, 4),
        checkNotNull(target, 5),
        checkNotNull(filterType, 6));
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
