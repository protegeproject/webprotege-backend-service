package edu.stanford.protege.webprotege.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Action;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@JsonTypeName("LogOutUser")
public class LogOutUserAction implements Action<LogOutUserResult> {

    @JsonCreator
    public LogOutUserAction() {
    }

    public static LogOutUserAction create() {
        return new LogOutUserAction();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode("LogOutUserAction");
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof LogOutUserAction;
    }


    @Override
    public String toString() {
        return toStringHelper("LogOutUserAction")
                .toString();
    }
}
