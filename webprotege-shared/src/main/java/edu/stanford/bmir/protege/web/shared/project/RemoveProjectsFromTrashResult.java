package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasEventListResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectMovedFromTrashEvent;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class RemoveProjectsFromTrashResult extends AbstractHasEventListResult<WebProtegeEvent<?>> {

    private RemoveProjectsFromTrashResult() {
    }

    public RemoveProjectsFromTrashResult(EventList<WebProtegeEvent<?>> eventList) {
        super(eventList);
    }
}
