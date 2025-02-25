package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.forms.data.FormControlDataDto;
import edu.stanford.protege.webprotege.forms.data.FormEntitySubject;
import edu.stanford.protege.webprotege.forms.data.TextControlDataDto;
import edu.stanford.protege.webprotege.forms.data.TextControlDataDtoComparator;
import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;
import edu.stanford.protege.webprotege.projectsettings.ProjectSettings;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
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
                     .filter(l -> isApplicable(textControlDescriptor, l))
                     .map(literal -> TextControlDataDto.get(textControlDescriptor, literal, depth))
                     .sorted(textControlDataDtoComparator)
                     .collect(ImmutableList.toImmutableList());
    }

    private static boolean isApplicable(@Nonnull TextControlDescriptor textControlDescriptor, OWLLiteral l) {
        if (!isSpecificLangString(textControlDescriptor)) {
            return true;
        }
        var requiredLangTag = textControlDescriptor.getSpecificLangTag();
        if(requiredLangTag.isEmpty()) {
            return true;
        }
        return l.getLang().equalsIgnoreCase(requiredLangTag);
    }

    private static boolean isSpecificLangString(@Nonnull TextControlDescriptor textControlDescriptor) {
        return textControlDescriptor.getStringType().equals(StringType.SPECIFIC_LANG_STRING);
    }

}