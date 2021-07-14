package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.frame.translator.AxiomPropertyValueTranslator;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.PropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class EntityRelationshipMatcherFactory {
  private final Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider;

  private final Provider<SubClassOfAxiomsBySubClassIndex> subClassOfAxiomsBySubClassIndexProvider;

  private final Provider<PropertyAssertionAxiomsBySubjectIndex>
      propertyAssertionAxiomsBySubjectIndexProvider;

  private final Provider<AxiomPropertyValueTranslator> axiomTranslatorProvider;

  @Inject
  public EntityRelationshipMatcherFactory(
      Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider,
      Provider<SubClassOfAxiomsBySubClassIndex> subClassOfAxiomsBySubClassIndexProvider,
      Provider<PropertyAssertionAxiomsBySubjectIndex> propertyAssertionAxiomsBySubjectIndexProvider,
      Provider<AxiomPropertyValueTranslator> axiomTranslatorProvider) {
    this.projectOntologiesIndexProvider = checkNotNull(projectOntologiesIndexProvider, 1);
    this.subClassOfAxiomsBySubClassIndexProvider =
        checkNotNull(subClassOfAxiomsBySubClassIndexProvider, 2);
    this.propertyAssertionAxiomsBySubjectIndexProvider =
        checkNotNull(propertyAssertionAxiomsBySubjectIndexProvider, 3);
    this.axiomTranslatorProvider = checkNotNull(axiomTranslatorProvider, 4);
  }

  public EntityRelationshipMatcher create(
      RelationshipPresence relationshipPresence, PropertyValueMatcher propertyValueMatcher) {
    return new EntityRelationshipMatcher(
        checkNotNull(projectOntologiesIndexProvider.get(), 1),
        checkNotNull(relationshipPresence, 2),
        checkNotNull(propertyValueMatcher, 3),
        checkNotNull(subClassOfAxiomsBySubClassIndexProvider.get(), 4),
        checkNotNull(propertyAssertionAxiomsBySubjectIndexProvider.get(), 5),
        checkNotNull(axiomTranslatorProvider.get(), 6));
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
