package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
@AutoValue

@JsonTypeName("UpdateFormDescriptor")
public abstract class UpdateFormDescriptorResult implements Result {

    @JsonCreator
    public static UpdateFormDescriptorResult create() {
        return new AutoValue_UpdateFormDescriptorResult();
    }
}
