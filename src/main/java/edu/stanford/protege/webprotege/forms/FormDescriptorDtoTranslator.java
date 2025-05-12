package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.FormRegionAccessRestrictionsList;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.FormRootSubject;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.UserCapabilities;
import edu.stanford.protege.webprotege.forms.data.FormFieldAccessMode;
import edu.stanford.protege.webprotege.forms.field.*;

import javax.annotation.Nonnull;

import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import jakarta.inject.Inject;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableList.toImmutableList;

@FormDataBuilderSession
public class FormDescriptorDtoTranslator {

    @Nonnull
    private final FormControlDescriptorVisitor<FormControlDescriptorDto> translatorVisitor;

    @Nonnull
    private final UserCapabilities userCapabilities;

    @Nonnull
    private final FormRegionAccessRestrictionsList accessRestrictions;

    @Nonnull
    private final EntityMatcherFactory entityMatcherFactory;

    @Nonnull
    private FormRootSubject rootSubject;

    @Inject
    public FormDescriptorDtoTranslator(@Nonnull ChoiceDescriptorCache choiceDescriptorCache,
                                       @Nonnull UserCapabilities userCapabilities,
                                       @Nonnull FormRegionAccessRestrictionsList accessRestrictions,
                                       @Nonnull EntityMatcherFactory entityMatcherFactory, @Nonnull FormRootSubject rootSubject) {
        this.translatorVisitor = new FormControlDescriptorVisitor<>() {
            @Override
            public FormControlDescriptorDto visit(TextControlDescriptor textControlDescriptor) {
                return TextControlDescriptorDto.get(textControlDescriptor);
            }

            @Override
            public FormControlDescriptorDto visit(NumberControlDescriptor numberControlDescriptor) {
                return NumberControlDescriptorDto.get(numberControlDescriptor);
            }

            @Override
            public FormControlDescriptorDto visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                var choices = choiceDescriptorCache.getChoices(singleChoiceControlDescriptor.getSource());

                return SingleChoiceControlDescriptorDto.get(singleChoiceControlDescriptor.getWidgetType(),
                                                            choices, singleChoiceControlDescriptor.getSource());
            }

            @Override
            public FormControlDescriptorDto visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                var choices = choiceDescriptorCache.getChoices(multiChoiceControlDescriptor.getSource());
                return MultiChoiceControlDescriptorDto.get(multiChoiceControlDescriptor.getSource(), choices);
            }

            @Override
            public FormControlDescriptorDto visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return EntityNameControlDescriptorDto.get(entityNameControlDescriptor.getPlaceholder(),
                                                          entityNameControlDescriptor.getMatchCriteria().orElse(null));
            }

            @Override
            public FormControlDescriptorDto visit(ImageControlDescriptor imageControlDescriptor) {
                return ImageControlDescriptorDto.get();
            }

            @Override
            public FormControlDescriptorDto visit(GridControlDescriptor gridControlDescriptor) {
                var columns = gridControlDescriptor.getColumns()
                                                   .stream()
                                                   .map(this::toGridColumnDescriptorDto)
                                                   .collect(toImmutableList());
                return GridControlDescriptorDto.get(columns, gridControlDescriptor.getSubjectFactoryDescriptor().orElseThrow());
            }

            private GridColumnDescriptorDto toGridColumnDescriptorDto(GridColumnDescriptor column) {
                return GridColumnDescriptorDto.get(column.getId(),
                                                   column.getOptionality(),
                                                   column.getRepeatability(),
                                                   column.getOwlBinding().orElse(null),
                                                   column.getLabel(),
                                                   column.getFormControlDescriptor().accept(this));
            }

            @Override
            public FormControlDescriptorDto visit(SubFormControlDescriptor subFormControlDescriptor) {
                return SubFormControlDescriptorDto.get(toFormDescriptorDto(subFormControlDescriptor.getFormDescriptor(), f -> true));
            }
        };
        this.userCapabilities = userCapabilities;
        this.accessRestrictions = accessRestrictions;
        this.entityMatcherFactory = entityMatcherFactory;
        this.rootSubject = rootSubject;
    }

    @Nonnull
    public FormFieldDescriptorDto toFormFieldDescriptorDto(@Nonnull FormFieldDescriptor descriptor) {
        return FormFieldDescriptorDto.get(descriptor.getId(),
                                          descriptor.getOwlBinding().orElse(null),
                                          descriptor.getLabel(),
                                          descriptor.getFieldRun(),
                                          toFormControlDescriptorDto(descriptor.getFormControlDescriptor()),
                                          descriptor.getOptionality(),
                                          descriptor.getRepeatability(),
                                          descriptor.getDeprecationStrategy(),
                                          descriptor.isReadOnly(),
                                          getFieldMode(descriptor),
                                          descriptor.getInitialExpansionState(),
                                          descriptor.getHelp());
    }

    @Nonnull
    public FormDescriptorDto toFormDescriptorDto(FormDescriptor descriptor, Predicate<FormFieldDescriptor> fieldFilter) {
        var fields = descriptor.getFields().stream()
                .filter(fieldFilter)
                .map(this::toFormFieldDescriptorDto).collect(toImmutableList());
        return FormDescriptorDto.get(descriptor.getFormId(),
                                     descriptor.getLabel(),
                                     fields,
                                     descriptor.getSubjectFactoryDescriptor().orElse(null));
    }

    @Nonnull
    public FormControlDescriptorDto toFormControlDescriptorDto(@Nonnull FormControlDescriptor descriptor) {
        return descriptor.accept(translatorVisitor);
    }

    private FormFieldAccessMode getFieldMode(FormFieldDescriptor field) {
        // Does the field have access restrictions on it, in general?
        if(!accessRestrictions.hasAccessRestrictions(field.getId(), FormRegionCapability.EDIT_FORM_REGION)) {
            // No restrictions at all
            return FormFieldAccessMode.READ_WRITE;
        }
        var formRegionCapabilities = userCapabilities.getFormRegionCapabilities(field.getId());
        var canEdit = formRegionCapabilities.stream()
                // Form region Id matches
                .filter(c -> c.formRegionId().equals(field.getId()))
                // Edit capability
                .filter(c -> c.id().equals(FormRegionCapability.EDIT_FORM_REGION))
                // Context matches
                .anyMatch(c -> entityMatcherFactory.getEntityMatcher(c.contextCriteria()).matches(rootSubject.subject()));

        if(canEdit) {
            return FormFieldAccessMode.READ_WRITE;
        }
        else {
            return FormFieldAccessMode.READ_ONLY;
        }
    }


}
