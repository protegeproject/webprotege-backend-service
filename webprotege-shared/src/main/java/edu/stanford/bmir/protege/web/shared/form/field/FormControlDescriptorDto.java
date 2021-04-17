package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.gwt.user.client.rpc.IsSerializable;

@JsonSubTypes({
        @Type(EntityNameControlDescriptorDto.class),
        @Type(GridControlDescriptorDto.class),
        @Type(ImageControlDescriptorDto.class),
        @Type(MultiChoiceControlDescriptorDto.class),
        @Type(NumberControlDescriptorDto.class),
        @Type(SingleChoiceControlDescriptorDto.class),
        @Type(TextControlDescriptorDto.class),
        @Type(SubFormControlDescriptorDto.class)

})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface FormControlDescriptorDto extends IsSerializable {

    <R> R accept(FormControlDescriptorDtoVisitor<R> visitor);

    FormControlDescriptor toFormControlDescriptor();
}
