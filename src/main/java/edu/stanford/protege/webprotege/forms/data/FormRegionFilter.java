package edu.stanford.protege.webprotege.forms.data;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.forms.PropertyNames;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue

public abstract class FormRegionFilter {

    @JsonCreator
    @Nonnull
    public static FormRegionFilter get(@JsonProperty(PropertyNames.REGION_ID) @Nonnull FormRegionId formRegionId,
                                       @JsonProperty(PropertyNames.CRITERIA) @Nonnull PrimitiveFormControlDataMatchCriteria matchCriteria) {
        return new AutoValue_FormRegionFilter(formRegionId, matchCriteria);
    }

    @JsonProperty(PropertyNames.REGION_ID)
    @Nonnull
    public abstract FormRegionId getFormRegionId();

    @JsonProperty(PropertyNames.CRITERIA)
    @Nonnull
    public abstract PrimitiveFormControlDataMatchCriteria getMatchCriteria();

}
