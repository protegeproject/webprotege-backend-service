package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.project.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class ProjectResourceFactory {
  private final Provider<ActionExecutor> executorProvider;

  private final Provider<AxiomsResourceFactory> axiomsResourceFactoryProvider;

  private final Provider<DeleteAxiomsResourceFactory> deleteAxiomsResourceFactoryProvider;

  private final Provider<RevisionsResourceFactory> changesResourceFactoryProvider;

  private final Provider<FormsResourceFactory> formsResourceFactoryProvider;

  private final Provider<ProjectSettingsResourceFactory> projectSettingsResourceFactoryProvider;

  @Inject
  public ProjectResourceFactory(
      Provider<ActionExecutor> executorProvider,
      Provider<AxiomsResourceFactory> axiomsResourceFactoryProvider,
      Provider<DeleteAxiomsResourceFactory> deleteAxiomsResourceFactoryProvider,
      Provider<RevisionsResourceFactory> changesResourceFactoryProvider,
      Provider<FormsResourceFactory> formsResourceFactoryProvider,
      Provider<ProjectSettingsResourceFactory> projectSettingsResourceFactoryProvider) {
    this.executorProvider = checkNotNull(executorProvider, 1);
    this.axiomsResourceFactoryProvider = checkNotNull(axiomsResourceFactoryProvider, 2);
    this.deleteAxiomsResourceFactoryProvider = checkNotNull(deleteAxiomsResourceFactoryProvider, 3);
    this.changesResourceFactoryProvider = checkNotNull(changesResourceFactoryProvider, 4);
    this.formsResourceFactoryProvider = checkNotNull(formsResourceFactoryProvider, 5);
    this.projectSettingsResourceFactoryProvider =
        checkNotNull(projectSettingsResourceFactoryProvider, 6);
  }

  public ProjectResource create(ProjectId projectId) {
    return new ProjectResource(
        checkNotNull(projectId, 1),
        checkNotNull(executorProvider.get(), 2),
        checkNotNull(axiomsResourceFactoryProvider.get(), 3),
        checkNotNull(deleteAxiomsResourceFactoryProvider.get(), 4),
        checkNotNull(changesResourceFactoryProvider.get(), 5),
        checkNotNull(formsResourceFactoryProvider.get(), 6),
        checkNotNull(projectSettingsResourceFactoryProvider.get(), 7));
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
