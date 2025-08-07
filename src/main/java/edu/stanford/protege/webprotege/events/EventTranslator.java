package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.revision.Revision;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public interface EventTranslator {

    /**
     * Prepare this translator for the following ontology changes.
     *
     * @param sessionId
     * @param submittedChanges The list of ontology changes to be submitted.  The actual changes that will be applied
     *                         will either be this list or a subset of this list.
     */
    void prepareForOntologyChanges(EventTranslatorSessionId sessionId, List<OntologyChange> submittedChanges);

    /**
     * Translate the ontology changes that were applied to high level project events.
     *
     * @param sessionId
     * @param revision         The revision
     * @param changes          The applied changes.
     * @param projectEventList A list to be filled with high level project events that were generated from the changes.
     * @param changeRequestId
     */
    void translateOntologyChanges(EventTranslatorSessionId sessionId, Revision revision,
                                  ChangeApplicationResult<?> changes,
                                  List<HighLevelProjectEventProxy> projectEventList,
                                  ChangeRequestId changeRequestId);

    void closeSession(EventTranslatorSessionId sessionId);
}

