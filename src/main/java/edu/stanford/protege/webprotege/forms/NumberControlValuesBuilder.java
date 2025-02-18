package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.NumberControlDataDto;
import edu.stanford.protege.webprotege.forms.data.NumberControlDataDtoComparator;
import edu.stanford.protege.webprotege.forms.field.NumberControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class NumberControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final NumberControlDataDtoComparator comparator;

    @Inject
    public NumberControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                      @Nonnull NumberControlDataDtoComparator comparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.comparator = checkNotNull(comparator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getNumberControlDataDtoValues(@Nonnull NumberControlDescriptor numberControlDescriptor,
                                                                           @Nonnull Optional<FormEntitySubject> subject,
                                                                           @Nonnull OwlBinding theBinding, int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        return values.stream()
                     .filter(p -> p instanceof OWLLiteral)
                     .map(p -> (OWLLiteral) p)
                     .map(value -> NumberControlDataDto.get(numberControlDescriptor, value, depth))
                     .sorted(comparator)
                     .collect(ImmutableList.toImmutableList());
    }
}