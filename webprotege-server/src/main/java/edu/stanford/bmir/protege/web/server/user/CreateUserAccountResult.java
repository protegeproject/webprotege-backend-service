package edu.stanford.bmir.protege.web.server.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue

@JsonTypeName("CreateUserAccount")
public abstract class CreateUserAccountResult implements Result {

    @JsonCreator
    public static CreateUserAccountResult create() {
        return new AutoValue_CreateUserAccountResult();
    }
}
