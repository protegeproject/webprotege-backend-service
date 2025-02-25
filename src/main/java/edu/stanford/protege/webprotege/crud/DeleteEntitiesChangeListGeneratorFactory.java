package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.util.EntityDeleter;
import java.util.Set;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.semanticweb.owlapi.model.OWLEntity;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class DeleteEntitiesChangeListGeneratorFactory {
  private final Provider<MessageFormatter> msgFormatterProvider;

  private final Provider<EntityDeleter> entityDeleterProvider;

  @Inject
  public DeleteEntitiesChangeListGeneratorFactory(
      Provider<MessageFormatter> msgFormatterProvider,
      Provider<EntityDeleter> entityDeleterProvider) {
    this.msgFormatterProvider = checkNotNull(msgFormatterProvider, 1);
    this.entityDeleterProvider = checkNotNull(entityDeleterProvider, 2);
  }

  public DeleteEntitiesChangeListGenerator create(Set<OWLEntity> entities, ChangeRequestId changeRequestId) {
    return new DeleteEntitiesChangeListGenerator(
            checkNotNull(msgFormatterProvider.get(), 1),
            checkNotNull(entityDeleterProvider.get(), 2),
            checkNotNull(entities, 3), changeRequestId);
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
