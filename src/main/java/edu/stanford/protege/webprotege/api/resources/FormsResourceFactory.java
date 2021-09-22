package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.common.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Deprecated
public final class FormsResourceFactory {
  private final Provider<ActionExecutor> executorProvider;

  private final Provider<FormResourceFactory> formResourceFactoryProvider;

  @Inject
  public FormsResourceFactory(
      Provider<ActionExecutor> executorProvider,
      Provider<FormResourceFactory> formResourceFactoryProvider) {
    this.executorProvider = checkNotNull(executorProvider, 1);
    this.formResourceFactoryProvider = checkNotNull(formResourceFactoryProvider, 2);
  }

  public FormsResource create(ProjectId projectId) {
    return new FormsResource(
        checkNotNull(projectId, 1),
        checkNotNull(executorProvider.get(), 2),
        checkNotNull(formResourceFactoryProvider.get(), 3));
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
