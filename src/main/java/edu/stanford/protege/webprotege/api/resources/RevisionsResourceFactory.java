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
public final class RevisionsResourceFactory {
  private final Provider<ActionExecutor> executorProvider;

  @Inject
  public RevisionsResourceFactory(Provider<ActionExecutor> executorProvider) {
    this.executorProvider = checkNotNull(executorProvider, 1);
  }

  public RevisionsResource create(ProjectId projectId) {
    return new RevisionsResource(
        checkNotNull(projectId, 1), checkNotNull(executorProvider.get(), 2));
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
