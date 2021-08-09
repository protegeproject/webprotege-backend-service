package edu.stanford.protege.webprotege.download;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.project.ProjectManager;
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import edu.stanford.protege.webprotege.user.UserId;
import java.nio.file.Path;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
final class CreateDownloadTaskFactory {
  private final Provider<ProjectManager> projectManagerProvider;

  private final Provider<ProjectDownloaderFactory> projectDownloaderFactoryProvider;

  @Inject
  CreateDownloadTaskFactory(
      Provider<ProjectManager> projectManagerProvider,
      Provider<ProjectDownloaderFactory> projectDownloaderFactoryProvider) {
    this.projectManagerProvider = checkNotNull(projectManagerProvider, 1);
    this.projectDownloaderFactoryProvider = checkNotNull(projectDownloaderFactoryProvider, 2);
  }

  CreateDownloadTask create(
      ProjectId projectId,
      UserId userId,
      String projectDisplayName,
      RevisionNumber revisionNumber,
      DownloadFormat format,
      Path destinationPath) {
    return new CreateDownloadTask(
        checkNotNull(projectManagerProvider.get(), 1),
        checkNotNull(projectId, 2),
        checkNotNull(userId, 3),
        checkNotNull(projectDisplayName, 4),
        checkNotNull(revisionNumber, 5),
        checkNotNull(format, 6),
        checkNotNull(destinationPath, 7),
        checkNotNull(projectDownloaderFactoryProvider.get(), 8));
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
