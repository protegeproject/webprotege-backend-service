package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.event.WebProtegeEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class DirtyChangedEvent extends WebProtegeEvent<DirtyChangedHandler> {

    public DirtyChangedEvent() {
    }

    @Override
    protected void dispatch(DirtyChangedHandler handler) {
        handler.handleDirtyChanged(this);
    }
}
