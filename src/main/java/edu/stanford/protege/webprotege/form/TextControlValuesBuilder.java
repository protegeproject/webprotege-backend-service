package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.form.data.FormControlDataDto;
import edu.stanford.protege.webprotege.form.data.FormEntitySubject;
import edu.stanford.protege.webprotege.form.data.TextControlDataDto;
import edu.stanford.protege.webprotege.form.data.TextControlDataDtoComparator;
import edu.stanford.protege.webprotege.form.field.OwlBinding;
import edu.stanford.protege.webprotege.form.field.TextControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class TextControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final TextControlDataDtoComparator textControlDataDtoComparator;

    @Inject
    public TextControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                    @Nonnull TextControlDataDtoComparator textControlDataDtoComparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.textControlDataDtoComparator = checkNotNull(textControlDataDtoComparator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getTextControlDataDtoValues(@Nonnull TextControlDescriptor textControlDescriptor,
                                                                         @Nonnull Optional<FormEntitySubject> subject,
                                                                         @Nonnull OwlBinding theBinding,
                                                                         int depth) {
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        return values.stream()
                     .filter(p -> p instanceof OWLLiteral)
                     .map(p -> (OWLLiteral) p)
                     .map(literal -> TextControlDataDto.get(textControlDescriptor, literal, depth))
                     .sorted(textControlDataDtoComparator)
                     .collect(ImmutableList.toImmutableList());
    }
}