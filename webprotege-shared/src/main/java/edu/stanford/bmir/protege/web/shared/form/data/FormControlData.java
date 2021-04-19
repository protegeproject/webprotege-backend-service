package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@JsonSubTypes({
        @Type(EntityNameControlData.class),
        @Type(FormData.class),
        @Type(GridControlData.class),
        @Type(ImageControlData.class),
        @Type(MultiChoiceControlData.class),
        @Type(NumberControlData.class),
        @Type(SingleChoiceControlData.class),
        @Type(TextControlData.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface FormControlData extends IsSerializable {

    <R> R accept(@Nonnull FormControlDataVisitorEx<R> visitor);

    void accept(@Nonnull FormControlDataVisitor visitor);
}
