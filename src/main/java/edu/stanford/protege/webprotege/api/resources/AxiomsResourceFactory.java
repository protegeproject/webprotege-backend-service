package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.axioms.PostedAxiomsActionExecutor;
import edu.stanford.protege.webprotege.common.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class AxiomsResourceFactory {
  private final Provider<PostedAxiomsActionExecutor> postedAxiomsActionExecutorProvider;

  @Inject
  public AxiomsResourceFactory(
      Provider<PostedAxiomsActionExecutor> postedAxiomsActionExecutorProvider) {
    this.postedAxiomsActionExecutorProvider = checkNotNull(postedAxiomsActionExecutorProvider, 1);
  }

  public AxiomsResource create(ProjectId projectId) {
    return new AxiomsResource(
        checkNotNull(postedAxiomsActionExecutorProvider.get(), 1), checkNotNull(projectId, 2));
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
