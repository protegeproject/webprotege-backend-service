package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.FormRegionAccessRestrictionsList;
import edu.stanford.protege.webprotege.forms.EntityFormDataRequestSpec.UserCapabilities;
import edu.stanford.protege.webprotege.forms.data.*;
import edu.stanford.protege.webprotege.forms.field.*;
import edu.stanford.protege.webprotege.common.LangTagFilter;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageCollector;
import edu.stanford.protege.webprotege.common.PageRequest;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-10-31
 */
@FormDataBuilderSession
public class EntityFrameFormDataDtoBuilder {

    private static final Logger logger = LoggerFactory.getLogger(EntityFrameFormDataDtoBuilder.class);

    @Nonnull
    private final FormDataBuilderSessionRenderer sessionRenderer;

    @Nonnull
    private final TextControlValuesBuilder textControlValuesBuilder;

    @Nonnull
    private final NumberControlValuesBuilder numberControlValuesBuilder;

    @Nonnull
    private final MultiChoiceControlValueBuilder multiChoiceControlValueBuilder;

    @Nonnull
    private final SingleChoiceControlValuesBuilder SingleChoiceControlValuesBuilder;

    @Nonnull
    private final EntityNameControlValuesBuilder entityNameControlValuesBuilder;

    @Nonnull
    private final ImageControlValuesBuilder imageControlValuesBuilder;

    @Nonnull
    private final GridControlValuesBuilder gridControlValuesBuilder;

    @Nonnull
    private final SubFormControlValuesBuilder subFormControlValuesBuilder;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex formPageRequestIndex;

    @Nonnull
    private final FormRegionFilterIndex formRegionFilterIndex;

    private final UserCapabilities capabilities;

    private final FormDescriptorDtoTranslator formDataDtoTranslator;

    @Nonnull
    private final FormRegionAccessRestrictionsList formRegionAccessRestrictionsList;

    @Inject
    public EntityFrameFormDataDtoBuilder(@Nonnull FormDataBuilderSessionRenderer sessionRenderer,
                                         @Nonnull TextControlValuesBuilder textControlValuesBuilder,
                                         @Nonnull NumberControlValuesBuilder numberControlValuesBuilder,
                                         @Nonnull MultiChoiceControlValueBuilder multiChoiceControlValueBuilder,
                                         @Nonnull SingleChoiceControlValuesBuilder singleChoiceControlValuesBuilder,
                                         @Nonnull EntityNameControlValuesBuilder entityNameControlValuesBuilder,
                                         @Nonnull ImageControlValuesBuilder imageControlValuesBuilder,
                                         @Nonnull GridControlValuesBuilder gridControlValuesBuilder,
                                         @Nonnull SubFormControlValuesBuilder subFormControlValuesBuilder,
                                         @Nonnull LangTagFilter langTagFilter,
                                         @Nonnull FormPageRequestIndex formPageRequestIndex,
                                         @Nonnull FormRegionFilterIndex formRegionFilterIndex,
                                         @Nonnull FormDescriptorDtoTranslator formDataDtoTranslator,
                                         @Nonnull UserCapabilities userCapabilities,
                                         @Nonnull FormRegionAccessRestrictionsList formRegionAccessRestrictionsList) {
        this.sessionRenderer = sessionRenderer;
        this.textControlValuesBuilder = textControlValuesBuilder;
        this.numberControlValuesBuilder = numberControlValuesBuilder;
        this.multiChoiceControlValueBuilder = multiChoiceControlValueBuilder;
        SingleChoiceControlValuesBuilder = singleChoiceControlValuesBuilder;
        this.entityNameControlValuesBuilder = entityNameControlValuesBuilder;
        this.imageControlValuesBuilder = imageControlValuesBuilder;
        this.gridControlValuesBuilder = gridControlValuesBuilder;
        this.subFormControlValuesBuilder = subFormControlValuesBuilder;
        this.langTagFilter = langTagFilter;
        this.formPageRequestIndex = formPageRequestIndex;
        this.formRegionFilterIndex = formRegionFilterIndex;
        this.capabilities = userCapabilities;
        this.formDataDtoTranslator = formDataDtoTranslator;
        this.formRegionAccessRestrictionsList = formRegionAccessRestrictionsList;
    }

    @Nonnull
    protected ImmutableList<FormControlDataDto> toFormControlValues(@Nonnull Optional<FormEntitySubject> subject,
                                                                    @Nonnull FormRegionId formFieldId,
                                                                    @Nonnull BoundControlDescriptor descriptor,
                                                                    int depth) {
        var owlBinding = descriptor.getOwlBinding();
        if (owlBinding.isEmpty()) {
            return ImmutableList.of();
        }
        var theBinding = owlBinding.get();
        var formControlDescriptor = descriptor.getFormControlDescriptor();
        return formControlDescriptor.accept(new FormControlDescriptorVisitor<>() {
            @Override
            public ImmutableList<FormControlDataDto> visit(TextControlDescriptor textControlDescriptor) {
                return textControlValuesBuilder.getTextControlDataDtoValues(textControlDescriptor,
                                                                            subject,
                                                                            theBinding,
                                                                            depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(NumberControlDescriptor numberControlDescriptor) {
                return numberControlValuesBuilder.getNumberControlDataDtoValues(numberControlDescriptor,
                                                                                subject,
                                                                                theBinding,
                                                                                depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(SingleChoiceControlDescriptor singleChoiceControlDescriptor) {
                return SingleChoiceControlValuesBuilder.getSingleChoiceControlDataDtoValues(
                        singleChoiceControlDescriptor,
                        subject,
                        theBinding,
                        depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(MultiChoiceControlDescriptor multiChoiceControlDescriptor) {
                return multiChoiceControlValueBuilder.getMultiChoiceControlDataDtoValues(multiChoiceControlDescriptor,
                                                                                         subject,
                                                                                         theBinding,
                                                                                         depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(EntityNameControlDescriptor entityNameControlDescriptor) {
                return entityNameControlValuesBuilder.getEntityNameControlDataDtoValues(entityNameControlDescriptor,
                                                                                        subject,
                                                                                        theBinding,
                                                                                        depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(ImageControlDescriptor imageControlDescriptor) {
                return imageControlValuesBuilder.getImageControlDataDtoValues(imageControlDescriptor,
                                                                              subject,
                                                                              theBinding,
                                                                              depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(GridControlDescriptor gridControlDescriptor) {
                return gridControlValuesBuilder.getGridControlDataDtoValues(gridControlDescriptor,
                                                                            subject,
                                                                            theBinding,
                                                                            formFieldId,
                                                                            depth);
            }

            @Override
            public ImmutableList<FormControlDataDto> visit(SubFormControlDescriptor subFormControlDescriptor) {
                return subFormControlValuesBuilder.getSubFormControlDataDtoValues(subFormControlDescriptor,
                                                                                  subject,
                                                                                  theBinding,
                                                                                  depth);
            }
        });
    }


    public FormDataDto toFormData(@Nonnull Optional<FormEntitySubject> subject,
                                  @Nonnull FormDescriptor formDescriptor) {
        logger.info("Current user capabilities: {}", capabilities);
        int depth = 0;
        return getFormDataDto(subject, formDescriptor, depth);
    }

    protected FormDataDto getFormDataDto(@Nonnull Optional<FormEntitySubject> subject,
                                         @Nonnull FormDescriptor formDescriptor,
                                         int depth) {
        var formSubject = subject.map(s -> {
            var renderedSubject = sessionRenderer.getEntityRendering(s.getEntity());
            return FormSubjectDto.getFormSubject(renderedSubject);
        });
        var fieldData = formDescriptor.getFields().stream().filter(this::userCanViewFormRegion).map(field -> {

            var formControlValues = toFormControlValues(formSubject.map(FormSubjectDto::toFormSubject),
                                                        field.getId(),
                                                        field,
                                                        depth);
            var controlValuesStream = formControlValues.stream().filter(this::isIncluded);
            var pageRequest = subject.map(s -> formPageRequestIndex.getPageRequest(s,
                                                                                   field.getId(),
                                                                                   FormPageRequest.SourceType.CONTROL_STACK))
                                     .orElseGet(PageRequest::requestFirstPage);
            var controlValuesPage = controlValuesStream.collect(PageCollector.toPage(pageRequest.getPageNumber(),
                                                                                     pageRequest.getPageSize()))
                                                       .orElse(Page.emptyPage());
            var fieldDescriptorDto = formDataDtoTranslator.toFormFieldDescriptorDto(field);
            return FormFieldDataDto.get(fieldDescriptorDto, controlValuesPage);
        }).collect(toImmutableList());
        var formDescriptorDto = formDataDtoTranslator.toFormDescriptorDto(formDescriptor);
        return formSubject.map(s -> FormDataDto.get(s, formDescriptorDto, fieldData, depth))
                          .orElseGet(() -> FormDataDto.get(formDescriptorDto, fieldData, depth));
    }

    private boolean userCanViewFormRegion(FormFieldDescriptor field) {
        // Does the field have view access restrictions on it, in general?
        if(!formRegionAccessRestrictionsList.hasAccessRestrictions(field.getId(), "ViewFormRegion")) {
            return true;
        }
        var formRegionCapabilities = capabilities.getFormRegionCapabilities(field.getId());
        // Can the current user's capabilities meet the view access restrictions?
        return formRegionCapabilities.stream().anyMatch(c -> c.id().equals("ViewFormRegion"));
    }

    private boolean isIncluded(@Nonnull FormControlDataDto formControlData) {

        FormControlDataLangTagBasedInclusion formControlDataLangTagBasedInclusion = new FormControlDataLangTagBasedInclusion(
                langTagFilter);
        return formControlDataLangTagBasedInclusion.isIncluded(formControlData);

    }
}
