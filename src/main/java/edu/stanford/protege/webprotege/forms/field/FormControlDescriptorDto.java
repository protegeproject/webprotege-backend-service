package edu.stanford.protege.webprotege.forms.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonSubTypes({@Type(EntityNameControlDescriptorDto.class), @Type(GridControlDescriptorDto.class), @Type(ImageControlDescriptorDto.class), @Type(MultiChoiceControlDescriptorDto.class), @Type(NumberControlDescriptorDto.class), @Type(SingleChoiceControlDescriptorDto.class), @Type(TextControlDescriptorDto.class), @Type(SubFormControlDescriptorDto.class)

})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface FormControlDescriptorDto {

    <R> R accept(FormControlDescriptorDtoVisitor<R> visitor);

    FormControlDescriptor toFormControlDescriptor();
}
