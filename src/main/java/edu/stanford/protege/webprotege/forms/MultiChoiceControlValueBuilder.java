package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.MultiChoiceControlDataDto;
import edu.stanford.protege.webprotege.forms.field.MultiChoiceControlDescriptor;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class MultiChoiceControlValueBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final PrimitiveFormControlDataDtoRenderer renderer;

    @Inject
    public MultiChoiceControlValueBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                          @Nonnull PrimitiveFormControlDataDtoRenderer renderer) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getMultiChoiceControlDataDtoValues(@Nonnull MultiChoiceControlDescriptor multiChoiceControlDescriptor,
                                                                         @Nonnull Optional<FormEntitySubject> subject,
                                                                         @Nonnull OwlBinding theBinding,
                                                                                int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        var vals = values.stream()
                         .flatMap(renderer::toFormControlDataDto)
                         .collect(ImmutableList.toImmutableList());
        return ImmutableList.of(MultiChoiceControlDataDto.get(multiChoiceControlDescriptor,
                                                              vals,
                                                              depth));
    }
}