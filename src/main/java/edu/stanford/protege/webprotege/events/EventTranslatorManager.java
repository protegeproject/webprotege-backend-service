package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.revision.Revision;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public class EventTranslatorManager {

    private final Collection<EventTranslator> eventTranslators;

    @Inject
    public EventTranslatorManager(Set<EventTranslator> eventTranslators) {
        this.eventTranslators = eventTranslators;
    }

    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
        for(EventTranslator eventTranslator : eventTranslators) {
            eventTranslator.prepareForOntologyChanges(submittedChanges);
        }
    }

    public void translateOntologyChanges(Revision revision, ChangeApplicationResult<?> appliedChanges, List<HighLevelProjectEventProxy> projectEventList) {
        for(EventTranslator eventTranslator : eventTranslators) {
            eventTranslator.translateOntologyChanges(revision, appliedChanges, projectEventList);
        }
    }
}
