package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.common.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class ProjectSettingsResourceFactory {
  private final Provider<ActionExecutor> actionExecutorProvider;

  @Inject
  public ProjectSettingsResourceFactory(Provider<ActionExecutor> actionExecutorProvider) {
    this.actionExecutorProvider = checkNotNull(actionExecutorProvider, 1);
  }

  public ProjectSettingsResource create(ProjectId projectId) {
    return new ProjectSettingsResource(
        checkNotNull(projectId, 1), checkNotNull(actionExecutorProvider.get(), 2));
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
