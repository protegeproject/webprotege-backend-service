package edu.stanford.bmir.protege.web.shared.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("CreateUserAccount")
public abstract class CreateUserAccountResult implements Result {

    @JsonCreator
    public static CreateUserAccountResult create() {
        return new AutoValue_CreateUserAccountResult();
    }
}
