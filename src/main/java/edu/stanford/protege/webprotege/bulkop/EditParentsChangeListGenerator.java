package edu.stanford.protege.webprotege.bulkop;


import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class EditParentsChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final ChangeRequestId changeRequestId;

    @Nonnull
    private final ImmutableSet<OWLClass> parents;

    @Nonnull
    private final OWLClass entity;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologies;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassAxiomIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final String commitMessage;

    @Inject
    public EditParentsChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                          @Nonnull ImmutableSet<OWLClass> parents,
                                          @Nonnull OWLClass entity,
                                          @Nonnull String commitMessage,
                                          @Nonnull ProjectOntologiesIndex projectOntologies,
                                          @Nonnull SubClassOfAxiomsBySubClassIndex subClassAxiomIndex,
                                          @Nonnull OWLDataFactory dataFactory) {
        this.changeRequestId = changeRequestId;
        this.parents = checkNotNull(parents);
        this.entity = checkNotNull(entity);
        this.projectOntologies = checkNotNull(projectOntologies);
        this.subClassAxiomIndex = checkNotNull(subClassAxiomIndex);
        this.dataFactory = checkNotNull(dataFactory);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        var changeList = new OntologyChangeList.Builder<Boolean>();

        projectOntologies.getOntologyIds().forEach(ontId -> {
            var currentSubClassOfAxiomList = subClassAxiomIndex
                    .getSubClassOfAxiomsForSubClass(entity, ontId)
                    .filter(ax -> ax.getSuperClass().isNamed()).toList();

            var currentParents = new HashSet<OWLClass>();
            for (OWLSubClassOfAxiom axiom : currentSubClassOfAxiomList) {
                OWLClassExpression superClass = axiom.getSuperClass();
                if (superClass.isClassExpressionLiteral()) {
                    currentParents.add(superClass.asOWLClass());
                }
            }


            var parentsToAdd = new HashSet<>(parents);
            parentsToAdd.removeAll(currentParents);
            addParentsToEntity(parentsToAdd, ontId, changeList);

            var parentsToRemove = new HashSet<>(currentParents);
            parentsToRemove.removeAll(parents);
            removeParentsFromEntity(currentSubClassOfAxiomList, parentsToRemove, ontId, changeList);
        });

        return changeList.build(true);
    }

    private void removeParentsFromEntity(List<OWLSubClassOfAxiom> currentSubClassOfAxiomList,
                                         Set<OWLClass> parentsToRemove,
                                         OWLOntologyID ontId,
                                         OntologyChangeList.Builder<Boolean> changeList) {

        currentSubClassOfAxiomList.stream()
                .filter(axiom -> axiom.getSuperClass().isNamed())
                .filter(axiom -> parentsToRemove.contains(axiom.getSuperClass().asOWLClass()))
                .forEach(axiom -> addRemoveSubClassOfAxiomToChangeList(axiom, ontId, changeList));

    }

    private void addParentsToEntity(Set<OWLClass> parentsToAdd,
                                    OWLOntologyID ontId,
                                    OntologyChangeList.Builder<Boolean> changeList) {
        parentsToAdd.forEach(newParent -> addAddSubClassOfAxiomToChangeList(entity, newParent, ontId, changeList));
    }

    private void addAddSubClassOfAxiomToChangeList(OWLClass subclass,
                                                OWLClass parent,
                                                OWLOntologyID ontId,
                                                OntologyChangeList.Builder<Boolean> changeList) {
        var newAx = dataFactory.getOWLSubClassOfAxiom(subclass, parent);
        changeList.addAxiom(ontId, newAx);
    }

    private void addRemoveSubClassOfAxiomToChangeList(OWLSubClassOfAxiom ax,
                                                     OWLOntologyID ontId,
                                                     OntologyChangeList.Builder<Boolean> changeList) {
        changeList.removeAxiom(ontId, ax);
    }

    @Override
    public Boolean getRenamedResult(Boolean result,
                                    RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return commitMessage;
    }
}
