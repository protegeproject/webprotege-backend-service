package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.forms.data.FormSubject;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
@JsonTypeName("FormPageRequest")
public record FormPageRequest(@JsonProperty(PropertyNames.FORM_ID) FormId formId,
                              @JsonProperty(PropertyNames.SUBJECT) FormSubject subject,
                              @JsonProperty(PropertyNames.REGION_ID) FormRegionId regionId,
                              @JsonProperty(PropertyNames.SOURCE_TYPE) SourceType sourceType,
                              @JsonProperty(PropertyNames.PAGE_REQUEST) PageRequest pageRequest) {

    public static final int DEFAULT_PAGE_SIZE = 10;

    @JsonCreator
    @Nonnull
    public static FormPageRequest get(@JsonProperty(PropertyNames.FORM_ID) @Nonnull FormId formId,
                                      @JsonProperty(PropertyNames.SUBJECT) @Nonnull FormSubject subject,
                                      @JsonProperty(PropertyNames.REGION_ID) @Nonnull FormRegionId formFieldId,
                                      @JsonProperty(PropertyNames.SOURCE_TYPE) @Nonnull SourceType sourceType,
                                      @JsonProperty(PropertyNames.PAGE_REQUEST) @Nonnull PageRequest pageRequest) {
        return new FormPageRequest(formId, subject, formFieldId, sourceType, pageRequest);
    }

    public enum SourceType {
        CONTROL_STACK, GRID_CONTROL
    }
}
