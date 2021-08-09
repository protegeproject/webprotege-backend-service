package edu.stanford.protege.webprotege.download;

import edu.stanford.protege.webprotege.project.PrefixDeclarationsStore;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class ProjectDownloaderFactory {
  private final Provider<PrefixDeclarationsStore> prefixDeclarationsStoreProvider;

  @Inject
  public ProjectDownloaderFactory(
      Provider<PrefixDeclarationsStore> prefixDeclarationsStoreProvider) {
    this.prefixDeclarationsStoreProvider = checkNotNull(prefixDeclarationsStoreProvider, 1);
  }

  public ProjectDownloader create(
      ProjectId projectId,
      String fileName,
      RevisionNumber revision,
      DownloadFormat format,
      RevisionManager revisionManager) {
    return new ProjectDownloader(
        checkNotNull(projectId, 1),
        checkNotNull(fileName, 2),
        checkNotNull(revision, 3),
        checkNotNull(format, 4),
        checkNotNull(revisionManager, 5),
        checkNotNull(prefixDeclarationsStoreProvider.get(), 6));
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
