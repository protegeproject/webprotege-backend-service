package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("SetEntityGraphActiveFilters")
public abstract class SetEntityGraphActiveFiltersResult implements Result {

    @JsonCreator
    public static SetEntityGraphActiveFiltersResult create() {
        return new AutoValue_SetEntityGraphActiveFiltersResult();
    }
}
