package edu.stanford.protege.webprotege.index.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import edu.stanford.protege.webprotege.change.AxiomChange;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.DependentIndex;
import edu.stanford.protege.webprotege.index.DeprecatedEntitiesByEntityIndex;
import edu.stanford.protege.webprotege.index.Index;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class DeprecatedEntitiesByEntityIndexImpl implements DeprecatedEntitiesByEntityIndex, DependentIndex, UpdatableIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final Multimap<OWLAnnotationSubject, OWLAnnotationAssertionAxiom> map;

    @Inject
    public DeprecatedEntitiesByEntityIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.map = HashMultimap.create();
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        changes.stream()
               .filter(OntologyChange::isAxiomChange)
               .map(chg -> (AxiomChange) chg)
               .filter(chg -> chg.getAxiom() instanceof OWLAnnotationAssertionAxiom)
               .filter(chg -> ((OWLAnnotationAssertionAxiom) chg.getAxiom()).isDeprecatedIRIAssertion())
               .forEach(chg -> {
                   var ax = (OWLAnnotationAssertionAxiom) chg.getAxiom();
                   if(chg.isAddAxiom()) {
                       map.put(ax.getSubject(), ax);
                   }
                   else {
                       map.remove(ax.getSubject(), ax);
                   }
               });
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex);
    }

    @Override
    public boolean isDeprecated(@Nonnull OWLEntity entity) {
        return map.containsKey(entity.getIRI());
    }
}
