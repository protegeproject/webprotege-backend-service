package edu.stanford.bmir.protege.web.shared.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.auth.AbstractAuthenticationResult;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/02/15
 */
@JsonTypeName("CreateUserAccount")
public class CreateUserAccountResult extends AbstractAuthenticationResult {

    @JsonCreator
    public CreateUserAccountResult() {
    }

    @Override
    public int hashCode() {
        return Objects.hashCode("CreateUserAccountResult");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof CreateUserAccountResult;
    }


    @Override
    public String toString() {
        return toStringHelper("CreateUserAccountResult")
                .toString();
    }
}
