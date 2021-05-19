package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.protege.webprotege.form.data.EntityNameControlDataDto;
import edu.stanford.protege.webprotege.form.data.EntityNameControlDataDtoComparator;
import edu.stanford.protege.webprotege.form.data.FormControlDataDto;
import edu.stanford.protege.webprotege.form.data.FormEntitySubject;
import edu.stanford.protege.webprotege.form.field.EntityNameControlDescriptor;
import edu.stanford.protege.webprotege.form.field.OwlBinding;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

@FormDataBuilderSession
public class EntityNameControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Nonnull
    private final FormDataBuilderSessionRenderer renderer;

    @Nonnull
    private final EntityNameControlDataDtoComparator comparator;

    @Inject
    public EntityNameControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                          @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                          @Nonnull FormDataBuilderSessionRenderer renderer,
                                          @Nonnull EntityNameControlDataDtoComparator comparator) {
        this.bindingValuesExtractor = bindingValuesExtractor;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.renderer = renderer;
        this.comparator = comparator;
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getEntityNameControlDataDtoValues(@Nonnull EntityNameControlDescriptor entityNameControlDescriptor,
                                                                               @Nonnull Optional<FormEntitySubject> subject,
                                                                               @Nonnull OwlBinding theBinding,
                                                                               int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        return values.stream()
                     // Allow IRIs which correspond to entities
                     .filter(p -> p instanceof OWLEntity || p instanceof IRI)
                     .flatMap(p -> {
                         if (p instanceof OWLEntity) {
                             return Stream.of((OWLEntity) p);
                         } else {
                             var iri = (IRI) p;
                             return entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(iri);
                         }
                     })
                     .map(renderer::getEntityRendering)
                     .map(entity -> EntityNameControlDataDto.get(entityNameControlDescriptor, entity, depth))
                     .sorted(comparator)
                     .collect(ImmutableList.toImmutableList());
    }
}