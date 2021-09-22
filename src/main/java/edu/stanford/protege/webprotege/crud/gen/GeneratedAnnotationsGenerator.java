package edu.stanford.protege.webprotege.crud.gen;

import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSettings;
import edu.stanford.protege.webprotege.crud.EntityIriPrefixCriteriaRewriter;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-02
 */
public class GeneratedAnnotationsGenerator {

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Nonnull
    private final EntityIriPrefixCriteriaRewriter rewriter;

    @Nonnull
    private final IncrementingPatternDescriptorValueGenerator incrementingPatternDescriptorValueGenerator;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public GeneratedAnnotationsGenerator(@Nonnull MatcherFactory matcherFactory,
                                         @Nonnull EntityIriPrefixCriteriaRewriter rewriter,
                                         @Nonnull IncrementingPatternDescriptorValueGenerator incrementingPatternDescriptorValueGenerator,
                                         @Nonnull OWLDataFactory dataFactory,
                                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.matcherFactory = checkNotNull(matcherFactory);
        this.rewriter = checkNotNull(rewriter);
        this.incrementingPatternDescriptorValueGenerator = checkNotNull(incrementingPatternDescriptorValueGenerator);
        this.dataFactory = checkNotNull(dataFactory);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }


    public <E> void generateAnnotations(@Nonnull OWLEntity entity,
                                    @Nonnull Collection<OWLEntity> parents,
                                    @Nonnull EntityCrudKitSettings<?> crudKitSettings,
                                    @Nonnull OntologyChangeList.Builder<E> changeListBuilder) {

        var generatedAnnotationSettings = crudKitSettings.getGeneratedAnnotationsSettings();

        for(var descriptor : generatedAnnotationSettings.getDescriptors()) {
            if(isActivatedForEntity(entity, parents, descriptor)) {
                var chg = generateNextValueAndOntologyChange(entity, descriptor);
                changeListBuilder.add(chg);
            }
        }
    }

    @Nonnull
    private AddAxiomChange generateNextValueAndOntologyChange(@Nonnull OWLEntity entity,
                                                              GeneratedAnnotationDescriptor descriptor) {
        var annotationProperty = descriptor.getProperty();
        var generatedValueDescriptor = descriptor.getValueDescriptor();
        var value = getValue(annotationProperty, generatedValueDescriptor);
        var ax = dataFactory.getOWLAnnotationAssertionAxiom(annotationProperty,
                                                   entity.getIRI(),
                                                   value);
        var ontId = defaultOntologyIdManager.getDefaultOntologyId();
        return AddAxiomChange.of(ontId, ax);
    }

    private boolean isActivatedForEntity(@Nonnull OWLEntity entity,
                                         @Nonnull Collection<OWLEntity> parents,
                                         @Nonnull GeneratedAnnotationDescriptor descriptor) {
        return descriptor.getActivatedBy()
                  .map(criteria -> {
                      var rewrittenCriteria = rewriter.rewriteCriteria(criteria);
                      var matcher = matcherFactory.getMatcher(rewrittenCriteria);
                      return parents.stream()
                              .allMatch(matcher::matches);
                  })
                  .orElse(false);
    }

    private OWLAnnotationValue getValue(OWLAnnotationProperty annotationProperty, GeneratedValueDescriptor generatedValueDescriptor) {
        return generatedValueDescriptor.accept(new GeneratedValueDescriptorVisitor<OWLLiteral>() {
            @Override
            public OWLLiteral visit(@Nonnull IncrementingPatternDescriptor descriptor) {
                return incrementingPatternDescriptorValueGenerator.generateNextValue(annotationProperty,
                                                                                     descriptor);

            }
        });
    }
}
