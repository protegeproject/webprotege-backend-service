package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue

@JsonTypeName("CopyFormDescriptorsResult")
public abstract class CopyFormDescriptorsResult implements Result {

    public abstract ImmutableList<FormDescriptor> getCopiedFormDescriptors();

    @JsonCreator
    public static CopyFormDescriptorsResult create(@JsonProperty("copiedFormDescriptors") ImmutableList<FormDescriptor> copiedFormDescriptors) {
        return new AutoValue_CopyFormDescriptorsResult(copiedFormDescriptors);
    }


}
