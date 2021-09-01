package edu.stanford.protege.webprotege.frame;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.common.ProjectEvent;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameResult implements Result, HasEventList<ProjectEvent> {

    private EventList<ProjectEvent> eventList;

    private String frameText;


    private SetManchesterSyntaxFrameResult() {
    }

    private SetManchesterSyntaxFrameResult(EventList<ProjectEvent> eventList, String frameText) {
        this.eventList = checkNotNull(eventList);
        this.frameText = checkNotNull(frameText);
    }

    public static SetManchesterSyntaxFrameResult create(EventList<ProjectEvent> eventList, String frameText) {
        return new SetManchesterSyntaxFrameResult(eventList, frameText);
    }

    public String getFrameText() {
        return frameText;
    }

    @Override
    public EventList<ProjectEvent> getEventList() {
        return eventList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventList, frameText);
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
        return this.frameText.equals(other.frameText) && this.eventList.equals(other.eventList);
    }


    @Override
    public String toString() {
        return toStringHelper("SetManchesterSyntaxFrameResult")
                .add("frameText", frameText)
                .addValue(eventList)
                .toString();
    }
}
