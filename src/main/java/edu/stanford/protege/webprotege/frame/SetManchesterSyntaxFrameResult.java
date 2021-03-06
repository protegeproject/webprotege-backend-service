package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.dispatch.Result;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameResult implements Result {

    private String frameText;


    private SetManchesterSyntaxFrameResult() {
    }

    private SetManchesterSyntaxFrameResult(String frameText) {
        this.frameText = checkNotNull(frameText);
    }

    public static SetManchesterSyntaxFrameResult create(String frameText) {
        return new SetManchesterSyntaxFrameResult(frameText);
    }

    public String getFrameText() {
        return frameText;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetManchesterSyntaxFrameResult)) {
            return false;
        }
        SetManchesterSyntaxFrameResult other = (SetManchesterSyntaxFrameResult) obj;
        return this.frameText.equals(other.frameText);
    }


    @Override
    public String toString() {
        return toStringHelper("SetManchesterSyntaxFrameResult")
                .add("frameText", frameText)
                .toString();
    }
}
