package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

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
