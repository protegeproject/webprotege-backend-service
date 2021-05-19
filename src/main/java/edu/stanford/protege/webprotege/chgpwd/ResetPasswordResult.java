package edu.stanford.protege.webprotege.chgpwd;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import edu.stanford.protege.webprotege.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
@JsonTypeName("ResetPassword")
public class ResetPasswordResult implements Result {

    private ResetPasswordResultCode resultCode;

    /**
     * For serialization purposes only
     */
    private ResetPasswordResult() {
    }

    private ResetPasswordResult(ResetPasswordResultCode resultCode) {
        this.resultCode = checkNotNull(resultCode);
    }

    public static ResetPasswordResult create(ResetPasswordResultCode resultCode) {
        return new ResetPasswordResult(resultCode);
    }

    public ResetPasswordResultCode getResultCode() {
        return resultCode;
    }


    @Override
    public int hashCode() {
        return "ResetPasswordResult".hashCode() + resultCode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ResetPasswordResult)) {
            return false;
        }
        ResetPasswordResult other = (ResetPasswordResult) o;
        return this.resultCode == other.resultCode;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ResetPasswordResult").addValue(resultCode).toString();
    }
}
