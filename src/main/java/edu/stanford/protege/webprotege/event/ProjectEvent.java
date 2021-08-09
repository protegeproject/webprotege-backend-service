package edu.stanford.protege.webprotege.event;

import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public abstract class ProjectEvent<H> extends WebProtegeEvent<H> implements HasProjectId {

    protected ProjectEvent(ProjectId source) {
        setSource(source);
    }

    protected ProjectEvent() {
    }


    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return getSource();
    }
}
