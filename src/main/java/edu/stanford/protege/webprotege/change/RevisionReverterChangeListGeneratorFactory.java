package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class RevisionReverterChangeListGeneratorFactory {
  private final Provider<RevisionManager> revisionManagerProvider;

  @Inject
  public RevisionReverterChangeListGeneratorFactory(
      Provider<RevisionManager> revisionManagerProvider) {
    this.revisionManagerProvider = checkNotNull(revisionManagerProvider, 1);
  }

  public RevisionReverterChangeListGenerator create(RevisionNumber revisionNumber) {
    return new RevisionReverterChangeListGenerator(
        checkNotNull(revisionNumber, 1), checkNotNull(revisionManagerProvider.get(), 2));
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
