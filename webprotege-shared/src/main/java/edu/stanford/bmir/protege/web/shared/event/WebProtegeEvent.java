package edu.stanford.bmir.protege.web.shared.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "eventType")
public abstract class WebProtegeEvent<H> {

    private ProjectId projectId;

    protected WebProtegeEvent() {
    }

    protected abstract void dispatch(H handler);

    public ProjectId getSource() {
        return projectId;
    }

    protected void setSource(ProjectId source) {
        this.projectId = source;
    }
}
