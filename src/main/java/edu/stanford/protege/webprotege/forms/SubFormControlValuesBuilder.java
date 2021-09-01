package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.field.OwlBinding;
import edu.stanford.protege.webprotege.forms.field.SubFormControlDescriptor;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class SubFormControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider;


    @Nonnull
    private final FormSubjectTranslator formSubjectTranslator;

    @Inject
    public SubFormControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                       @Nonnull Provider<EntityFrameFormDataDtoBuilder> formDataDtoBuilderProvider,
                                       @Nonnull FormSubjectTranslator formSubjectTranslator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.formDataDtoBuilderProvider = checkNotNull(formDataDtoBuilderProvider);
        this.formSubjectTranslator = checkNotNull(formSubjectTranslator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getSubFormControlDataDtoValues(@Nonnull SubFormControlDescriptor subFormControlDescriptor,
                                                                            @Nonnull Optional<FormEntitySubject> subject,
                                                                            @Nonnull OwlBinding theBinding,
                                                                            int depth) {
        var subFormDescriptor = subFormControlDescriptor.getFormDescriptor();
        var values = bindingValuesExtractor.getBindingValues(subject, theBinding);
        if(values.isEmpty()) {
            return ImmutableList.of(
                    formDataDtoBuilderProvider.get()
                                              .getFormDataDto(Optional.empty(), subFormDescriptor, depth + 1)
            );
        }
        // There has to be a specification of the subject
        var subjectEntityType = subFormControlDescriptor.getFormDescriptor()
                .getSubjectFactoryDescriptor()
                .map(FormSubjectFactoryDescriptor::getEntityType)
                .orElseThrow();
        return values.stream()
                     .map(primitive -> toFormSubject(primitive, subjectEntityType))
                     .map(subSubject -> formDataDtoBuilderProvider.get()
                                                                  .getFormDataDto(subSubject, subFormDescriptor, depth))
                     .collect(ImmutableList.toImmutableList());
    }

    private Optional<FormEntitySubject> toFormSubject(@Nonnull OWLPrimitive primitive,
                                                @Nonnull EntityType<?> entityType) {
        return formSubjectTranslator.getSubjectForPrimitive(primitive, entityType);
    }
}