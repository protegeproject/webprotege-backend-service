package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.entity.EntityRenamer;
import edu.stanford.protege.webprotege.index.ProjectSignatureIndex;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class FindAndReplaceIRIPrefixChangeGeneratorFactory {
  private final Provider<ProjectSignatureIndex> projectSignatureIndexProvider;

  private final Provider<EntityRenamer> entityRenamerProvider;

  @Inject
  public FindAndReplaceIRIPrefixChangeGeneratorFactory(
      Provider<ProjectSignatureIndex> projectSignatureIndexProvider,
      Provider<EntityRenamer> entityRenamerProvider) {
    this.projectSignatureIndexProvider = checkNotNull(projectSignatureIndexProvider, 1);
    this.entityRenamerProvider = checkNotNull(entityRenamerProvider, 2);
  }

  public FindAndReplaceIRIPrefixChangeGenerator create(ChangeRequestId changeRequestId, String fromPrefix, String toPrefix) {
    return new FindAndReplaceIRIPrefixChangeGenerator(changeRequestId, checkNotNull(fromPrefix, 1),
                                                      checkNotNull(toPrefix, 2),
                                                      checkNotNull(projectSignatureIndexProvider.get(), 3),
                                                      checkNotNull(entityRenamerProvider.get(), 4));
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
