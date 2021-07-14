package edu.stanford.protege.webprotege.obo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@AutoValue

@JsonTypeName("SetOboTermXRefs")
public abstract class SetOboTermXRefsResult implements Result {

    @JsonCreator
    public static SetOboTermXRefsResult create() {
        return new AutoValue_SetOboTermXRefsResult();
    }
}
