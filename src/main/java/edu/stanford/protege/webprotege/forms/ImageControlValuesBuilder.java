package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.ImageControlDataDto;
import edu.stanford.protege.webprotege.forms.data.ImageControlDataDtoComparator;
import edu.stanford.protege.webprotege.forms.field.ImageControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

@FormDataBuilderSession
public class ImageControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final ImageControlDataDtoComparator comparator;

    @Inject
    public ImageControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor, @Nonnull ImageControlDataDtoComparator imageControlDataDtoComparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.comparator = checkNotNull(imageControlDataDtoComparator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getImageControlDataDtoValues(ImageControlDescriptor imageControlDescriptor,
                                                                          @Nonnull Optional<FormEntitySubject> subject,
                                                                          OwlBinding theBinding, int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        return values.stream()
                     .filter(p -> p instanceof IRI)
                     .map(p -> (IRI) p)
                     .map(iri -> ImageControlDataDto.get(imageControlDescriptor, iri, depth))
                     .sorted(comparator)
                     .collect(toImmutableList());
    }
}