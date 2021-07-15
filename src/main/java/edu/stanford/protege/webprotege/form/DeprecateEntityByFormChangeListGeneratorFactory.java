package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.entity.EntityRenamer;
import edu.stanford.protege.webprotege.form.data.FormData;
import edu.stanford.protege.webprotege.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubAnnotationPropertyAxiomsBySubPropertyIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.protege.webprotege.index.SubDataPropertyAxiomsBySubPropertyIndex;
import edu.stanford.protege.webprotege.index.SubObjectPropertyAxiomsBySubPropertyIndex;
import edu.stanford.protege.webprotege.inject.ProjectComponent;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.projectsettings.EntityDeprecationSettings;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import java.util.Optional;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

public final class DeprecateEntityByFormChangeListGeneratorFactory {
  private final Provider<EntityDeleter> entityDeleterProvider;

  private final Provider<EntityFormChangeListGeneratorFactory>
      formChangeListGeneratorFactoryProvider;

  private final Provider<EntityFormManager> entityFormManagerProvider;

  private final Provider<MessageFormatter> messageFormatterProvider;

  private final Provider<ProjectId> projectIdProvider;

  private final Provider<ProjectComponent> projectComponentProvider;

  private final Provider<EntityRenamer> entityRenamerProvider;

  private final Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider;

  private final Provider<OWLDataFactory> dataFactoryProvider;

  private final Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider;

  private final Provider<SubClassOfAxiomsBySubClassIndex> subClassOfAxiomsBySubClassIndexProvider;

  private final Provider<SubObjectPropertyAxiomsBySubPropertyIndex>
      subObjectPropertyAxiomsBySubPropertyIndexProvider;

  private final Provider<SubDataPropertyAxiomsBySubPropertyIndex>
      subDataPropertyAxiomsBySubPropertyIndexProvider;

  private final Provider<SubAnnotationPropertyAxiomsBySubPropertyIndex>
      subAnnotationPropertyAxiomsBySubPropertyIndexProvider;

  private final Provider<ClassAssertionAxiomsByIndividualIndex>
      classAssertionAxiomsByIndividualIndexProvider;

  @Inject
  public DeprecateEntityByFormChangeListGeneratorFactory(
      Provider<EntityDeleter> entityDeleterProvider,
      Provider<EntityFormChangeListGeneratorFactory> formChangeListGeneratorFactoryProvider,
      Provider<EntityFormManager> entityFormManagerProvider,
      Provider<MessageFormatter> messageFormatterProvider,
      Provider<ProjectId> projectIdProvider,
      Provider<ProjectComponent> projectComponentProvider,
      Provider<EntityRenamer> entityRenamerProvider,
      Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider,
      Provider<OWLDataFactory> dataFactoryProvider,
      Provider<ProjectOntologiesIndex> projectOntologiesIndexProvider,
      Provider<SubClassOfAxiomsBySubClassIndex> subClassOfAxiomsBySubClassIndexProvider,
      Provider<SubObjectPropertyAxiomsBySubPropertyIndex>
          subObjectPropertyAxiomsBySubPropertyIndexProvider,
      Provider<SubDataPropertyAxiomsBySubPropertyIndex>
          subDataPropertyAxiomsBySubPropertyIndexProvider,
      Provider<SubAnnotationPropertyAxiomsBySubPropertyIndex>
          subAnnotationPropertyAxiomsBySubPropertyIndexProvider,
      Provider<ClassAssertionAxiomsByIndividualIndex>
          classAssertionAxiomsByIndividualIndexProvider) {
    this.entityDeleterProvider = checkNotNull(entityDeleterProvider, 1);
    this.formChangeListGeneratorFactoryProvider =
        checkNotNull(formChangeListGeneratorFactoryProvider, 2);
    this.entityFormManagerProvider = checkNotNull(entityFormManagerProvider, 3);
    this.messageFormatterProvider = checkNotNull(messageFormatterProvider, 4);
    this.projectIdProvider = checkNotNull(projectIdProvider, 5);
    this.projectComponentProvider = checkNotNull(projectComponentProvider, 6);
    this.entityRenamerProvider = checkNotNull(entityRenamerProvider, 7);
    this.defaultOntologyIdManagerProvider = checkNotNull(defaultOntologyIdManagerProvider, 8);
    this.dataFactoryProvider = checkNotNull(dataFactoryProvider, 9);
    this.projectOntologiesIndexProvider = checkNotNull(projectOntologiesIndexProvider, 10);
    this.subClassOfAxiomsBySubClassIndexProvider =
        checkNotNull(subClassOfAxiomsBySubClassIndexProvider, 11);
    this.subObjectPropertyAxiomsBySubPropertyIndexProvider =
        checkNotNull(subObjectPropertyAxiomsBySubPropertyIndexProvider, 12);
    this.subDataPropertyAxiomsBySubPropertyIndexProvider =
        checkNotNull(subDataPropertyAxiomsBySubPropertyIndexProvider, 13);
    this.subAnnotationPropertyAxiomsBySubPropertyIndexProvider =
        checkNotNull(subAnnotationPropertyAxiomsBySubPropertyIndexProvider, 14);
    this.classAssertionAxiomsByIndividualIndexProvider =
        checkNotNull(classAssertionAxiomsByIndividualIndexProvider, 15);
  }

  public DeprecateEntityByFormChangeListGenerator create(
      OWLEntity entityToBeDeprecated,
      Optional<FormData> deprecationFormData,
      Optional<OWLEntity> replacementEntity,
      EntityDeprecationSettings entityDeprecationSettings) {
    return new DeprecateEntityByFormChangeListGenerator(
        checkNotNull(entityToBeDeprecated, 1),
        checkNotNull(deprecationFormData, 2),
        checkNotNull(replacementEntity, 3),
        checkNotNull(entityDeprecationSettings, 4),
        checkNotNull(entityDeleterProvider.get(), 5),
        checkNotNull(formChangeListGeneratorFactoryProvider.get(), 6),
        checkNotNull(entityFormManagerProvider.get(), 7),
        checkNotNull(messageFormatterProvider.get(), 8),
        checkNotNull(projectIdProvider.get(), 9),
        checkNotNull(projectComponentProvider.get(), 10),
        checkNotNull(entityRenamerProvider.get(), 11),
        checkNotNull(defaultOntologyIdManagerProvider.get(), 12),
        checkNotNull(dataFactoryProvider.get(), 13),
        checkNotNull(projectOntologiesIndexProvider.get(), 14),
        checkNotNull(subClassOfAxiomsBySubClassIndexProvider.get(), 15),
        checkNotNull(subObjectPropertyAxiomsBySubPropertyIndexProvider.get(), 16),
        checkNotNull(subDataPropertyAxiomsBySubPropertyIndexProvider.get(), 17),
        checkNotNull(subAnnotationPropertyAxiomsBySubPropertyIndexProvider.get(), 18),
        checkNotNull(classAssertionAxiomsByIndividualIndexProvider.get(), 19));
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
