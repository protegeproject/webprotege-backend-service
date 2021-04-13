package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.app.UserInSession;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetCurrentUserInSession")
public abstract class GetCurrentUserInSessionResult implements Result {

    public abstract UserInSession getUserInSession();

    @JsonCreator
    public static GetCurrentUserInSessionResult create(@JsonProperty("userInSession") UserInSession userInSession) {
        return new AutoValue_GetCurrentUserInSessionResult(userInSession);
    }

}
