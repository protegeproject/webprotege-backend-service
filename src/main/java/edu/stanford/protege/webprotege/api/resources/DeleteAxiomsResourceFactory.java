package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.axioms.PostedAxiomsActionExecutor;
import edu.stanford.protege.webprotege.common.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Deprecated
public final class DeleteAxiomsResourceFactory {
  private final Provider<PostedAxiomsActionExecutor> postedAxiomsActionExecutorProvider;

  @Inject
  public DeleteAxiomsResourceFactory(
      Provider<PostedAxiomsActionExecutor> postedAxiomsActionExecutorProvider) {
    this.postedAxiomsActionExecutorProvider = checkNotNull(postedAxiomsActionExecutorProvider, 1);
  }

  public DeleteAxiomsResource create(ProjectId projectId) {
    return new DeleteAxiomsResource(
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
