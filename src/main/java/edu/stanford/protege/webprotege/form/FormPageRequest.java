package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.form.data.FormSubject;
import edu.stanford.protege.webprotege.form.field.FormRegionId;
import edu.stanford.protege.webprotege.pagination.PageRequest;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
@AutoValue

@JsonTypeName("FormPageRequest")
public abstract class FormPageRequest {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public enum SourceType {
        CONTROL_STACK,
        GRID_CONTROL
    }

    @JsonCreator
    @Nonnull
    public static FormPageRequest get(@JsonProperty("formId") @Nonnull FormId formId,
                                      @JsonProperty("subject") @Nonnull FormSubject subject,
                                      @JsonProperty("regionId") @Nonnull FormRegionId formFieldId,
                                      @JsonProperty("sourceType") @Nonnull SourceType sourceType,
                                      @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest) {
        return new AutoValue_FormPageRequest(formId, subject, formFieldId, sourceType, pageRequest);
    }

    @Nonnull
    public abstract FormId getFormId();

    @Nonnull
    public abstract FormSubject getSubject();

    @JsonProperty("regionId")
    @Nonnull
    public abstract FormRegionId getFieldId();

    @Nonnull
    public abstract SourceType getSourceType();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
