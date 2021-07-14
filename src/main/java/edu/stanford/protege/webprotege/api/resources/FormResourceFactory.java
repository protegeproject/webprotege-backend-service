package edu.stanford.protege.webprotege.api.resources;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.form.FormId;
import edu.stanford.protege.webprotege.project.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class FormResourceFactory {
  private final Provider<ActionExecutor> actionExecutorProvider;

  @Inject
  public FormResourceFactory(Provider<ActionExecutor> actionExecutorProvider) {
    this.actionExecutorProvider = checkNotNull(actionExecutorProvider, 1);
  }

  public FormResource create(ProjectId projectId, FormId formId) {
    return new FormResource(
        checkNotNull(projectId, 1),
        checkNotNull(formId, 2),
        checkNotNull(actionExecutorProvider.get(), 3));
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
