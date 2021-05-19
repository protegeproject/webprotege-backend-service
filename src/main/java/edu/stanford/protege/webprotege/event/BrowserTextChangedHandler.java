package edu.stanford.protege.webprotege.event;



/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public interface BrowserTextChangedHandler extends EventHandler {

    void browserTextChanged(BrowserTextChangedEvent event);
}
