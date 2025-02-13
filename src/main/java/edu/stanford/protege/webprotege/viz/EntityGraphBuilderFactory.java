package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.protege.webprotege.index.EquivalentClassesAxiomsIndex;
import edu.stanford.protege.webprotege.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class EntityGraphBuilderFactory {
  private final Provider<RenderingManager> rendererProvider;

  private final Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider;

  private final Provider<ObjectPropertyAssertionAxiomsBySubjectIndex>
      objectPropertyAssertionsProvider;

  private final Provider<SubClassOfAxiomsBySubClassIndex> subClassOfAxiomsProvider;

  private final Provider<ClassAssertionAxiomsByIndividualIndex> classAssertionAxiomsProvider;

  private final Provider<EquivalentClassesAxiomsIndex> equivalentClassesAxiomsProvider;

  private final Integer edgeLimit;

  @Inject
  public EntityGraphBuilderFactory(
      Provider<RenderingManager> rendererProvider,
      Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider,
      Provider<ObjectPropertyAssertionAxiomsBySubjectIndex> objectPropertyAssertionsProvider,
      Provider<SubClassOfAxiomsBySubClassIndex> subClassOfAxiomsProvider,
      Provider<ClassAssertionAxiomsByIndividualIndex> classAssertionAxiomsProvider,
      Provider<EquivalentClassesAxiomsIndex> equivalentClassesAxiomsProvider,
      @EntityGraphEdgeLimit Integer edgeLimit) {
    this.rendererProvider = checkNotNull(rendererProvider, 1);
    this.projectOntologiesIndexProvider = checkNotNull(projectOntologiesIndexProvider, 2);
    this.objectPropertyAssertionsProvider = checkNotNull(objectPropertyAssertionsProvider, 3);
    this.subClassOfAxiomsProvider = checkNotNull(subClassOfAxiomsProvider, 4);
    this.classAssertionAxiomsProvider = checkNotNull(classAssertionAxiomsProvider, 5);
    this.equivalentClassesAxiomsProvider = checkNotNull(equivalentClassesAxiomsProvider, 6);
    this.edgeLimit = checkNotNull(edgeLimit, 7);
  }

  public EntityGraphBuilder create(EdgeMatcher edgeMatcher) {
    return new EntityGraphBuilder(
        checkNotNull(rendererProvider.get(), 1),
        checkNotNull(projectOntologiesIndexProvider.get(), 2),
        checkNotNull(objectPropertyAssertionsProvider.get(), 3),
        checkNotNull(subClassOfAxiomsProvider.get(), 4),
        checkNotNull(classAssertionAxiomsProvider.get(), 5),
        checkNotNull(equivalentClassesAxiomsProvider.get(), 6),
        checkNotNull(edgeLimit, 7),
        checkNotNull(edgeMatcher, 8));
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
