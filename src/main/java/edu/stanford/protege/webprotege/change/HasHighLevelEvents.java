package edu.stanford.protege.webprotege.change;

import edu.stanford.protege.webprotege.events.HighLevelProjectEventProxy;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface HasHighLevelEvents {

    List<HighLevelProjectEventProxy> getHighLevelEvents();
}
