package edu.stanford.protege.webprotege.issues.mention;


import edu.stanford.protege.webprotege.issues.Mention;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
public class IssueMention extends Mention {

    private int issueNumber;


    public IssueMention(int issueNumber) {
        this.issueNumber = issueNumber;
    }


    private IssueMention() {
    }

    public int getIssueNumber() {
        return issueNumber;
    }


    @Override
    public String toString() {
        return toStringHelper("IssueMention")
                .add("issueNumber", issueNumber)
                .toString();
    }
}
