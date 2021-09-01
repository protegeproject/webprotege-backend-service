package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.forms.EntityFormChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataFactory;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class CreateEntityFromFormDataChangeListGeneratorFactory {
  private final Provider<EntityFormChangeListGeneratorFactory>
      formChangeListGeneratorFactoryProvider;

  private final Provider<OWLDataFactory> dataFactoryProvider;

  private final Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider;

  private final Provider<RenderingManager> renderingManagerProvider;

  @Inject
  public CreateEntityFromFormDataChangeListGeneratorFactory(
      Provider<EntityFormChangeListGeneratorFactory> formChangeListGeneratorFactoryProvider,
      Provider<OWLDataFactory> dataFactoryProvider,
      Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider,
      Provider<RenderingManager> renderingManagerProvider) {
    this.formChangeListGeneratorFactoryProvider =
        checkNotNull(formChangeListGeneratorFactoryProvider, 1);
    this.dataFactoryProvider = checkNotNull(dataFactoryProvider, 2);
    this.defaultOntologyIdManagerProvider = checkNotNull(defaultOntologyIdManagerProvider, 3);
    this.renderingManagerProvider = checkNotNull(renderingManagerProvider, 4);
  }

  public CreateEntityFromFormDataChangeListGenerator create(
      EntityType<?> entityType, FreshEntityIri freshEntityIri, FormData formData) {
    return new CreateEntityFromFormDataChangeListGenerator(
        checkNotNull(formChangeListGeneratorFactoryProvider.get(), 1),
        checkNotNull(dataFactoryProvider.get(), 2),
        checkNotNull(entityType, 3),
        checkNotNull(freshEntityIri, 4),
        checkNotNull(defaultOntologyIdManagerProvider.get(), 5),
        checkNotNull(formData, 6),
        checkNotNull(renderingManagerProvider.get(), 7));
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
