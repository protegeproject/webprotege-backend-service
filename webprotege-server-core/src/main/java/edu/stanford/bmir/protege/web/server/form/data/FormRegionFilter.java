package edu.stanford.bmir.protege.web.server.form.data;

import com.google.auto.value.AutoValue;


import edu.stanford.bmir.protege.web.server.form.field.FormRegionId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue

public abstract class FormRegionFilter {

    @Nonnull
    public static FormRegionFilter get(@Nonnull FormRegionId formRegionId,
                                       @Nonnull PrimitiveFormControlDataMatchCriteria matchCriteria) {
        return new AutoValue_FormRegionFilter(formRegionId, matchCriteria);
    }

    @Nonnull
    public abstract FormRegionId getFormRegionId();

    @Nonnull
    public abstract PrimitiveFormControlDataMatchCriteria getMatchCriteria();

}
