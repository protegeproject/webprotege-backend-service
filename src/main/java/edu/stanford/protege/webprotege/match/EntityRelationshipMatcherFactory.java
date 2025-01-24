package edu.stanford.protege.webprotege.match;

import edu.stanford.protege.webprotege.criteria.RelationshipPresence;
import edu.stanford.protege.webprotege.frame.translator.AxiomPropertyValueTranslator;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.PropertyAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

public final class EntityRelationshipMatcherFactory {
  private final ProjectOntologiesIndex projectOntologiesIndexProvider;

  private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndexProvider;

  private final PropertyAssertionAxiomsBySubjectIndex
      propertyAssertionAxiomsBySubjectIndexProvider;

  private final AxiomPropertyValueTranslator axiomTranslatorProvider;

  @Inject
  public EntityRelationshipMatcherFactory(
      ProjectOntologiesIndex projectOntologiesIndexProvider,
      SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndexProvider,
      PropertyAssertionAxiomsBySubjectIndex propertyAssertionAxiomsBySubjectIndexProvider,
      AxiomPropertyValueTranslator axiomTranslatorProvider) {
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
        checkNotNull(projectOntologiesIndexProvider, 1),
        checkNotNull(relationshipPresence, 2),
        checkNotNull(propertyValueMatcher, 3),
        checkNotNull(subClassOfAxiomsBySubClassIndexProvider, 4),
        checkNotNull(propertyAssertionAxiomsBySubjectIndexProvider, 5),
        checkNotNull(axiomTranslatorProvider, 6));
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
