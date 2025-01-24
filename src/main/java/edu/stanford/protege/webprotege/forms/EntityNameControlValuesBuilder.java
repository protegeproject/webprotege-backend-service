package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.EntityNameControlDataDto;
import edu.stanford.protege.webprotege.forms.data.EntityNameControlDataDtoComparator;
import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.field.EntityNameControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.*;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

@FormDataBuilderSession
public class EntityNameControlValuesBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EntityNameControlValuesBuilder.class);

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
        logger.debug("Getting entity name control values for {}", subject);
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        var dtos = values.stream()
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
                         .map(dto -> (FormControlDataDto) dto)
                     .collect(ImmutableList.toImmutableList());
        logger.debug("Computed {} values: {}", dtos.size(), dtos);
        return dtos;
    }
}