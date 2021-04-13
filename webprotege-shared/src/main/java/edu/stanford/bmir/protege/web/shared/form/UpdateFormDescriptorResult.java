package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("UpdateFormDescriptor")
public abstract class UpdateFormDescriptorResult implements Result {

    @JsonCreator
    public static UpdateFormDescriptorResult create() {
        return new AutoValue_UpdateFormDescriptorResult();
    }
}
