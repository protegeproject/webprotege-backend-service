package edu.stanford.bmir.protege.web.server.obo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
@AutoValue

@JsonTypeName("SetOboTermCrossProduct")
public abstract class SetOboTermCrossProductResult implements Result {

    @JsonCreator
    public static SetOboTermCrossProductResult create() {
        return new AutoValue_SetOboTermCrossProductResult();
    }
}
