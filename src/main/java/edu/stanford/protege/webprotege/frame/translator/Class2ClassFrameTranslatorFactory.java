package edu.stanford.protege.webprotege.frame.translator;

import edu.stanford.protege.webprotege.frame.ClassFrameTranslationOptions;
import edu.stanford.protege.webprotege.frame.PropertyValueMinimiser;
import edu.stanford.protege.webprotege.hierarchy.HasGetAncestors;
import edu.stanford.protege.webprotege.index.ClassFrameAxiomsIndex;
import edu.stanford.protege.webprotege.match.RelationshipMatcherFactory;
import javax.annotation.processing.Generated;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.semanticweb.owlapi.model.OWLClass;

@Generated(
  value = "com.google.auto.factory.processor.AutoFactoryProcessor",
  comments = "https://github.com/google/auto/tree/master/factory"
)
public final class Class2ClassFrameTranslatorFactory {
  private final Provider<ClassFrameAxiomsIndex> classFrameAxiomsIndexProvider;

  private final Provider<HasGetAncestors<OWLClass>> ancestorsProviderProvider;

  private final Provider<PropertyValueMinimiser> propertyValueMinimiserProvider;

  private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

  private final Provider<RelationshipMatcherFactory> matcherFactoryProvider;

  @Inject
  public Class2ClassFrameTranslatorFactory(
      Provider<ClassFrameAxiomsIndex> classFrameAxiomsIndexProvider,
      Provider<HasGetAncestors<OWLClass>> ancestorsProviderProvider,
      Provider<PropertyValueMinimiser> propertyValueMinimiserProvider,
      Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider,
      Provider<RelationshipMatcherFactory> matcherFactoryProvider) {
    this.classFrameAxiomsIndexProvider = checkNotNull(classFrameAxiomsIndexProvider, 1);
    this.ancestorsProviderProvider = checkNotNull(ancestorsProviderProvider, 2);
    this.propertyValueMinimiserProvider = checkNotNull(propertyValueMinimiserProvider, 3);
    this.axiomPropertyValueTranslatorProvider =
        checkNotNull(axiomPropertyValueTranslatorProvider, 4);
    this.matcherFactoryProvider = checkNotNull(matcherFactoryProvider, 5);
  }

  public Class2ClassFrameTranslator create(ClassFrameTranslationOptions options) {
    return new Class2ClassFrameTranslator(
        checkNotNull(classFrameAxiomsIndexProvider.get(), 1),
        checkNotNull(ancestorsProviderProvider.get(), 2),
        checkNotNull(propertyValueMinimiserProvider.get(), 3),
        checkNotNull(axiomPropertyValueTranslatorProvider.get(), 4),
        checkNotNull(matcherFactoryProvider.get(), 5),
        checkNotNull(options, 6));
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
