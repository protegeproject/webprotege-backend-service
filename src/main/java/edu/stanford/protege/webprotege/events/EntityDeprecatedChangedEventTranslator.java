package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.event.EntityDeprecationStatusChangedEvent;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.protege.webprotege.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.Revision;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;

import javax.inject.Inject;
import java.util.List;

import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/05/15
 */
public class EntityDeprecatedChangedEventTranslator implements EventTranslator {


    private final ProjectId projectId;

    private final DeprecatedEntityChecker deprecatedEntityChecker;

    private final EntitiesInProjectSignatureByIriIndex entitiesByIri;

    @Inject
    public EntityDeprecatedChangedEventTranslator(ProjectId projectId,
                                                  DeprecatedEntityChecker deprecatedEntityChecker,
                                                  EntitiesInProjectSignatureByIriIndex entitiesByIri) {
        this.projectId = projectId;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.entitiesByIri = entitiesByIri;
    }


    @Override
    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {

    }

    @Override
    public void translateOntologyChanges(Revision revision,
                                         ChangeApplicationResult<?> changes,
                                         List<HighLevelProjectEventProxy> projectEventList) {
        for(OntologyChange change : changes.getChangeList()) {
            if(change.isChangeFor(ANNOTATION_ASSERTION)) {
                var annotationAssertion = (OWLAnnotationAssertionAxiom) change.getAxiomOrThrow();
                if(annotationAssertion.getProperty()
                        .isDeprecated()) {
                    if(annotationAssertion.getSubject() instanceof IRI) {
                        IRI subject = (IRI) annotationAssertion.getSubject();
                        entitiesByIri.getEntitiesInSignature(subject)
                                     .map(entity -> {
                                         var deprecated = deprecatedEntityChecker.isDeprecated(entity);
                                         return new EntityDeprecationStatusChangedEvent(EventId.generate(), projectId, entity, deprecated);
                                     })
                                     .map(SimpleHighLevelProjectEventProxy::wrap)
                                     .forEach(projectEventList::add);
                    }
                }
            }
        }
    }
}
