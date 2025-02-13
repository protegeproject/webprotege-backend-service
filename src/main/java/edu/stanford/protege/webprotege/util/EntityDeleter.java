package edu.stanford.protege.webprotege.util;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.change.RemoveOntologyAnnotationChange;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public class EntityDeleter {

    @Nonnull
    private final ReferenceFinder referenceFinder;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public EntityDeleter(@Nonnull ReferenceFinder referenceFinder,
                         @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.referenceFinder = checkNotNull(referenceFinder);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    public ImmutableList<OntologyChange> getChangesToDeleteEntities(@Nonnull Collection<OWLEntity> entities) {
        checkNotNull(entities);
        if(entities.isEmpty()) {
            return ImmutableList.of();
        }
        return projectOntologiesIndex.getOntologyIds()
                .flatMap(ontologyID -> getChangesForOntology(entities, ontologyID))
                .collect(toImmutableList());
    }

    private Stream<OntologyChange> getChangesForOntology(Collection<OWLEntity> entities, OWLOntologyID ontology) {
        ReferenceFinder.ReferenceSet referenceSet = referenceFinder.getReferenceSet(entities, ontology);
        List<OntologyChange> changeList = new ArrayList<>(
                referenceSet.getReferencingAxioms().size() + referenceSet.getReferencingOntologyAnnotations().size()
        );
        for(OWLAxiom ax : referenceSet.getReferencingAxioms()) {
            changeList.add(RemoveAxiomChange.of(referenceSet.getOntologyId(), ax));
        }
        for(OWLAnnotation annotation : referenceSet.getReferencingOntologyAnnotations()) {
            changeList.add(RemoveOntologyAnnotationChange.of(referenceSet.getOntologyId(), annotation));
        }
        return changeList.stream();
    }
}
