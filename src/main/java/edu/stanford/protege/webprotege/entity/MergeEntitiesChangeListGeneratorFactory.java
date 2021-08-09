package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.issues.EntityDiscussionThreadRepository;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class MergeEntitiesChangeListGeneratorFactory {
  private final Provider<ProjectId> projectIdProvider;

  private final Provider<OWLDataFactory> dataFactoryProvider;

  private final Provider<EntityDiscussionThreadRepository> discussionThreadRepositoryProvider;

  private final Provider<EntityRenamer> entityRenamerProvider;

  private final Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider;

  private final Provider<ProjectOntologiesIndex> projectOntologiesProvider;

  private final Provider<AnnotationAssertionAxiomsBySubjectIndex> annotationAssertionsProvider;

  @Inject
  public MergeEntitiesChangeListGeneratorFactory(
      Provider<ProjectId> projectIdProvider,
      Provider<OWLDataFactory> dataFactoryProvider,
      Provider<EntityDiscussionThreadRepository> discussionThreadRepositoryProvider,
      Provider<EntityRenamer> entityRenamerProvider,
      Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider,
      Provider<ProjectOntologiesIndex> projectOntologiesProvider,
      Provider<AnnotationAssertionAxiomsBySubjectIndex> annotationAssertionsProvider) {
    this.projectIdProvider = checkNotNull(projectIdProvider, 1);
    this.dataFactoryProvider = checkNotNull(dataFactoryProvider, 2);
    this.discussionThreadRepositoryProvider = checkNotNull(discussionThreadRepositoryProvider, 3);
    this.entityRenamerProvider = checkNotNull(entityRenamerProvider, 4);
    this.defaultOntologyIdManagerProvider = checkNotNull(defaultOntologyIdManagerProvider, 5);
    this.projectOntologiesProvider = checkNotNull(projectOntologiesProvider, 6);
    this.annotationAssertionsProvider = checkNotNull(annotationAssertionsProvider, 7);
  }

  public MergeEntitiesChangeListGenerator create(
      ImmutableSet<OWLEntity> sourceEntities,
      OWLEntity targetEntity,
      MergedEntityTreatment treatment,
      String commitMessage) {
    return new MergeEntitiesChangeListGenerator(
        checkNotNull(sourceEntities, 1),
        checkNotNull(targetEntity, 2),
        checkNotNull(treatment, 3),
        checkNotNull(commitMessage, 4),
        checkNotNull(projectIdProvider.get(), 5),
        checkNotNull(dataFactoryProvider.get(), 6),
        checkNotNull(discussionThreadRepositoryProvider.get(), 7),
        checkNotNull(entityRenamerProvider.get(), 8),
        checkNotNull(defaultOntologyIdManagerProvider.get(), 9),
        checkNotNull(projectOntologiesProvider.get(), 10),
        checkNotNull(annotationAssertionsProvider.get(), 11));
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
