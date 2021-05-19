package edu.stanford.protege.webprotege.form;

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
public abstract class FormRegionPageRequest {

    @Nonnull
    public static FormRegionPageRequest get(@Nonnull FormSubject subject,
                                            @Nonnull FormRegionId formRegionId,
                                            @Nonnull FormPageRequest.SourceType sourceType,
                                            @Nonnull PageRequest pageRequest) {
        return new AutoValue_FormRegionPageRequest(subject, formRegionId, sourceType, pageRequest);
    }

    @Nonnull
    public abstract FormSubject getSubject();

    @Nonnull
    public abstract FormRegionId getFieldId();

    @Nonnull
    public abstract FormPageRequest.SourceType getSourceType();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
