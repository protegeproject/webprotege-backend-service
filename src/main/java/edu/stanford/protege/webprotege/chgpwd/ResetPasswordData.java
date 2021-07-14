package edu.stanford.protege.webprotege.chgpwd;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordData {

    private String emailAddress;

    /**
     * For serialization purposes only
     */
    private ResetPasswordData() {
    }

    public ResetPasswordData(String emailAddress) {
        this.emailAddress = checkNotNull(emailAddress);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public int hashCode() {
        return "ResetPasswordData".hashCode() + emailAddress.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ResetPasswordData)) {
            return false;
        }
        ResetPasswordData other = (ResetPasswordData) o;
        return this.emailAddress.equals(other.emailAddress);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ResetPasswordData").addValue(emailAddress).toString();
    }

}
