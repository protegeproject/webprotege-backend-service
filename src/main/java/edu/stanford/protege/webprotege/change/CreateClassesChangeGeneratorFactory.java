package edu.stanford.protege.webprotege.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import javax.annotation.processing.Generated;
import javax.inject.Inject;
import javax.inject.Provider;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class CreateClassesChangeGeneratorFactory {
  private final Provider<OWLDataFactory> dataFactoryProvider;

  private final Provider<MessageFormatter> msgProvider;

  private final Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider;

  @Inject
  public CreateClassesChangeGeneratorFactory(
      Provider<OWLDataFactory> dataFactoryProvider,
      Provider<MessageFormatter> msgProvider,
      Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider) {
    this.dataFactoryProvider = checkNotNull(dataFactoryProvider, 1);
    this.msgProvider = checkNotNull(msgProvider, 2);
    this.defaultOntologyIdManagerProvider = checkNotNull(defaultOntologyIdManagerProvider, 3);
  }

  public CreateClassesChangeGenerator create(
          String sourceText, String langTag, ImmutableSet<OWLClass> parent, ChangeRequestId changeRequestId) {
    return new CreateClassesChangeGenerator(
            checkNotNull(dataFactoryProvider.get(), 1),
            checkNotNull(msgProvider.get(), 2),
            checkNotNull(defaultOntologyIdManagerProvider.get(), 3),
            checkNotNull(sourceText, 4),
            checkNotNull(langTag, 5),
            checkNotNull(parent, 6), changeRequestId);
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
