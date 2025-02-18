package edu.stanford.protege.webprotege.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class CreateIndividualsChangeListGeneratorFactory {
  private final Provider<OWLDataFactory> dataFactoryProvider;

  private final Provider<MessageFormatter> msgProvider;

  private final Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider;

  @Inject
  public CreateIndividualsChangeListGeneratorFactory(
      Provider<OWLDataFactory> dataFactoryProvider,
      Provider<MessageFormatter> msgProvider,
      Provider<DefaultOntologyIdManager> defaultOntologyIdManagerProvider) {
    this.dataFactoryProvider = checkNotNull(dataFactoryProvider, 1);
    this.msgProvider = checkNotNull(msgProvider, 2);
    this.defaultOntologyIdManagerProvider = checkNotNull(defaultOntologyIdManagerProvider, 3);
  }

  public CreateIndividualsChangeListGenerator create(ImmutableSet<OWLClass> parents,
                                                     String sourceText,
                                                     String langTag,
                                                     ChangeRequestId changeRequestId) {
    return new CreateIndividualsChangeListGenerator(
            checkNotNull(dataFactoryProvider.get(), 1),
            checkNotNull(msgProvider.get(), 2),
            checkNotNull(defaultOntologyIdManagerProvider.get(), 3),
            checkNotNull(parents, 4),
            checkNotNull(sourceText, 5),
            checkNotNull(langTag, 6), changeRequestId);
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
