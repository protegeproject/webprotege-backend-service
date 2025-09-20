package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.revision.RevisionManager;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public class ProjectOntologiesIndexImpl implements ProjectOntologiesIndex, UpdatableIndex {

    private static final Logger logger = LoggerFactory.getLogger(ProjectOntologiesIndexImpl.class);

    @Nonnull
    private final Multiset<OWLOntologyID> ontologyIds = HashMultiset.create();

    @Nonnull
    private ImmutableList<OWLOntologyID> cache = ImmutableList.of();

    private boolean initialized = false;

    @Inject
    public ProjectOntologiesIndexImpl() {
    }

    @Nonnull
    @Override
    public synchronized Stream<OWLOntologyID> getOntologyIds() {
        if(!initialized) {
            throw new RuntimeException("Index not initialized");
        }
        return cache.stream();
    }

    public synchronized void init(RevisionManager revisionManager) {
        if(initialized) {
            return;
        }
        revisionManager.getRevisions()
                       .forEach(rev -> applyChanges(rev.getChanges()));
        initialized = true;
    }

    @Override
    public synchronized void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        for(var ontologyChange : changes) {
            if(ontologyChange.isAddAxiom() || ontologyChange.isAddOntologyAnnotation()) {
                ontologyIds.add(ontologyChange.ontologyId());
            }
            else if(ontologyChange.isRemoveAxiom() || ontologyChange.isRemoveOntologyAnnotation()) {
                ontologyIds.remove(ontologyChange.ontologyId());
            }
        }
        cache = ImmutableList.copyOf(ontologyIds.elementSet());
        initialized = true;
    }

    @Override
    public void reset() {
        ontologyIds.clear();
        initialized = false;
        cache = ImmutableList.of();
    }
}
