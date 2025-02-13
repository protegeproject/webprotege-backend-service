package edu.stanford.protege.webprotege.events;

import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.event.*;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureIndex;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.revision.HasGetRevisionSummary;
import edu.stanford.protege.webprotege.revision.Revision;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class HighLevelEventGenerator implements EventTranslator {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final HasGetRevisionSummary hasGetRevisionSummary;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesByIri;

    @Nonnull
    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    @Inject
    public HighLevelEventGenerator(@Nonnull ProjectId projectId,
                                   @Nonnull RenderingManager renderingManager,
                                   @Nonnull EntitiesInProjectSignatureByIriIndex entitiesByIri,
                                   @Nonnull HasGetRevisionSummary hasGetRevisionSummary,
                                   @Nonnull EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex) {
        this.projectId = checkNotNull(projectId);
        this.renderingManager = checkNotNull(renderingManager);
        this.entitiesByIri = checkNotNull(entitiesByIri);
        this.hasGetRevisionSummary = checkNotNull(hasGetRevisionSummary);
        this.entitiesInProjectSignatureIndex = checkNotNull(entitiesInProjectSignatureIndex);
    }

    @Override
    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
    }

    @Override
    public void translateOntologyChanges(Revision revision,
                                         ChangeApplicationResult<?> changes,
                                         final List<HighLevelProjectEventProxy> projectEventList,
                                         ChangeRequestId changeRequestId) {
        var changedEntities = new HashSet<OWLEntity>();
        var changedOntologies = new HashSet<OWLOntologyID>();
        changes.getChangeList()
               .forEach(change -> change.accept(new OntologyChangeVisitor() {
                   @Override
                   public void visit(@Nonnull AddAxiomChange addAxiomChange) {
                        handleAxiomChange(addAxiomChange.getAxiom());
                   }

                   @Override
                   public void visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
                        handleAxiomChange(removeAxiomChange.getAxiom());
                   }

                   @Override
                   public void visit(@Nonnull AddOntologyAnnotationChange addOntologyAnnotationChange) {
                        handleOntologyFrameChange(addOntologyAnnotationChange);
                   }

                   @Override
                   public void visit(@Nonnull RemoveOntologyAnnotationChange removeOntologyAnnotationChange) {
                        handleOntologyFrameChange(removeOntologyAnnotationChange);
                   }

                   @Override
                   public void visit(@Nonnull AddImportChange addImportChange) {
                        handleOntologyFrameChange(addImportChange);
                   }

                   @Override
                   public void visit(@Nonnull RemoveImportChange removeImportChange) {
                        handleOntologyFrameChange(removeImportChange);
                   }

                   private void handleAxiomChange(OWLAxiom axiom) {
                       var axiomSubjectProvider = new AxiomSubjectProvider();
                       var subject = axiomSubjectProvider.getSubject(axiom);
                       var entities = getEntitiesForSubject(subject);
                       entities.stream()
                               .filter(e -> !changedEntities.add(e))
                               .map(entity -> toFrameChangedEvent(changeRequestId, entity, revision))
                               .forEach(projectEventList::add);
                   }

                   private void handleOntologyFrameChange(OntologyChange change) {
//                       var ontologyId = change.getOntologyId();
//                       if(!changedOntologies.add(ontologyId)) {
//                           return;
//                       }
//                       var event = new OntologyFrameChangedEvent(projectId, ontologyId);
//                       projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(event));
                   }
               }));


        var changedEntitiesData = new HashSet<OWLEntityData>();
        var subject = changes.getSubject();
        if(subject instanceof OWLEntity) {
            var entity = (OWLEntity) subject;
            if(entitiesInProjectSignatureIndex.containsEntityInSignature(entity)) {
                changedEntitiesData.add(renderingManager.getRendering(entity));
            }
        }
        else if(subject instanceof OWLEntityData) {
            var entityData = (OWLEntityData) subject;
            if(entitiesInProjectSignatureIndex.containsEntityInSignature(entityData.getEntity())) {
                changedEntitiesData.add(entityData);
            }
        }
        else if(subject instanceof Collection) {
            var collection = (Collection<?>) subject;
            collection.stream()
                      .filter(element -> element instanceof OWLEntity)
                      .map(element -> (OWLEntity) element)
                      .filter(entitiesInProjectSignatureIndex::containsEntityInSignature)
                      .forEach(entity -> changedEntitiesData.add(renderingManager.getRendering(entity)));

        }
        var revisionSummary = hasGetRevisionSummary.getRevisionSummary(revision.getRevisionNumber());
        if(revisionSummary.isPresent()) {
            var event = new ProjectChangedEvent(EventId.generate(), projectId, revisionSummary.get(), changedEntitiesData);
            projectEventList.add(SimpleHighLevelProjectEventProxy.wrap(event));
        }
    }


    private Set<OWLEntity> getEntitiesForSubject(OWLObject subject) {
        Set<OWLEntity> entities;
        if(subject instanceof IRI) {
            entities = entitiesByIri.getEntitiesInSignature((IRI) subject)
                                    .collect(Collectors.toSet());
        }
        else if(subject instanceof OWLEntity) {
            entities = Collections.singleton((OWLEntity) subject);
        }
        else {
            entities = Collections.emptySet();
        }
        return entities;
    }

    private HighLevelProjectEventProxy toFrameChangedEvent(ChangeRequestId changeRequestId, OWLEntity e, Revision revision) {
        var event = e.accept(new OWLEntityVisitorEx<ProjectEvent>() {
            @Nonnull
            @Override
            public ProjectEvent visit(@Nonnull OWLClass cls) {
                return new ClassFrameChangedEvent(EventId.generate(), changeRequestId,  projectId, revision.getUserId(), cls);
            }

            @Nonnull
            @Override
            public ProjectEvent visit(@Nonnull OWLObjectProperty property) {
                return new ObjectPropertyFrameChangedEvent(EventId.generate(), projectId,
                                                           revision.getUserId(),
                                                           property);
            }

            @Nonnull
            @Override
            public ProjectEvent visit(@Nonnull OWLDataProperty property) {
                return new DataPropertyFrameChangedEvent(EventId.generate(), projectId,
                                                         revision.getUserId(),
                                                         property);
            }

            @Nonnull
            @Override
            public ProjectEvent visit(@Nonnull OWLNamedIndividual individual) {
                return new NamedIndividualFrameChangedEvent(EventId.generate(), projectId,
                                                            revision.getUserId(),
                                                            individual);
            }

            @Nonnull
            @Override
            public ProjectEvent visit(@Nonnull OWLDatatype datatype) {
                return new DatatypeFrameChangedEvent(EventId.generate(), projectId, revision.getUserId(), datatype);
            }

            @Nonnull
            @Override
            public ProjectEvent visit(@Nonnull OWLAnnotationProperty property) {
                return new AnnotationPropertyFrameChangedEvent(EventId.generate(),
                                                               projectId,
                                                               revision.getUserId(),
                                                               property);
            }
        });
        return SimpleHighLevelProjectEventProxy.wrap(event);
    }
}
