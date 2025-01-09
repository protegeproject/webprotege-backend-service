package edu.stanford.protege.webprotege.viz;

import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.common.ProjectId;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class DotRendererFactory {
  private final Provider<ProjectId> projectIdProvider;

  private final Provider<PlaceUrl> placeUrlProvider;

  @Inject
  public DotRendererFactory(
      Provider<ProjectId> projectIdProvider, Provider<PlaceUrl> placeUrlProvider) {
    this.projectIdProvider = checkNotNull(projectIdProvider, 1);
    this.placeUrlProvider = checkNotNull(placeUrlProvider, 2);
  }

  public DotRenderer create(EntityGraph graph) {
    return new DotRenderer(
        checkNotNull(projectIdProvider.get(), 1),
        checkNotNull(placeUrlProvider.get(), 2),
        checkNotNull(graph, 3));
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
