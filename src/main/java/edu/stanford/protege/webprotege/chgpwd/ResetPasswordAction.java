package edu.stanford.protege.webprotege.chgpwd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.dispatch.Action;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
@Deprecated
@JsonTypeName("ResetPassword")
public class ResetPasswordAction implements Action<ResetPasswordResult> {

    public static final String CHANNEL = "webprotege.users.ResetPassword";

    private ResetPasswordData resetPasswordData;

    private ResetPasswordAction(ResetPasswordData resetPasswordData) {
        this.resetPasswordData = checkNotNull(resetPasswordData);
    }

    @JsonCreator
    public static ResetPasswordAction create(@JsonProperty("resetPasswordData") ResetPasswordData resetPasswordData) {
        return new ResetPasswordAction(resetPasswordData);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    public ResetPasswordData getResetPasswordData() {
        return resetPasswordData;
    }

    @Override
    public int hashCode() {
        return "ResetPasswordAction".hashCode() + resetPasswordData.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ResetPasswordAction)) {
            return false;
        }
        ResetPasswordAction other = (ResetPasswordAction) o;
        return this.resetPasswordData.equals(other.resetPasswordData);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ResetPasswordAction").addValue(resetPasswordData).toString();
    }
}
