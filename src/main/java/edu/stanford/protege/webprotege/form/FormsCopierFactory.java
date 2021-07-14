package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.project.ProjectId;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class FormsCopierFactory {
  private final Provider<EntityFormRepository> entityFormRepositoryProvider;

  private final Provider<EntityFormSelectorRepository> entityFormSelectorRepositoryProvider;

  @Inject
  public FormsCopierFactory(
      Provider<EntityFormRepository> entityFormRepositoryProvider,
      Provider<EntityFormSelectorRepository> entityFormSelectorRepositoryProvider) {
    this.entityFormRepositoryProvider = checkNotNull(entityFormRepositoryProvider, 1);
    this.entityFormSelectorRepositoryProvider =
        checkNotNull(entityFormSelectorRepositoryProvider, 2);
  }

  public FormsCopier create(
      ProjectId fromProjectId, ProjectId toProjectId, ImmutableList<FormId> formsToCopy) {
    return new FormsCopier(
        checkNotNull(fromProjectId, 1),
        checkNotNull(toProjectId, 2),
        checkNotNull(formsToCopy, 3),
        checkNotNull(entityFormRepositoryProvider.get(), 4),
        checkNotNull(entityFormSelectorRepositoryProvider.get(), 5));
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
