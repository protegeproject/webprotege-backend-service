package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.annotation.Nonnull;

@JsonSubTypes({
        @Type(EntityNameControlDataDto.class),
        @Type(GridControlDataDto.class),
        @Type(ImageControlDataDto.class),
        @Type(MultiChoiceControlDataDto.class),
        @Type(NumberControlDataDto.class),
        @Type(SingleChoiceControlDataDto.class),
        @Type(TextControlDataDto.class),
        @Type(FormDataDto.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface FormControlDataDto {

    <R> R accept(FormControlDataDtoVisitorEx<R> visitor);

    @Nonnull
    FormControlData toFormControlData();

    int getDepth();
}
