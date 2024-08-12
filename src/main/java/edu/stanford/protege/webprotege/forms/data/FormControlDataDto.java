package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import edu.stanford.protege.webprotege.forms.PropertyNames;

import javax.annotation.Nonnull;

@JsonSubTypes({@Type(EntityNameControlDataDto.class), @Type(GridControlDataDto.class), @Type(ImageControlDataDto.class), @Type(MultiChoiceControlDataDto.class), @Type(NumberControlDataDto.class), @Type(SingleChoiceControlDataDto.class), @Type(TextControlDataDto.class), @Type(FormDataDto.class)})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public interface FormControlDataDto {

    <R> R accept(FormControlDataDtoVisitorEx<R> visitor);

    @Nonnull
    FormControlData toFormControlData();

    @JsonProperty(PropertyNames.DEPTH)
    int getDepth();
}
