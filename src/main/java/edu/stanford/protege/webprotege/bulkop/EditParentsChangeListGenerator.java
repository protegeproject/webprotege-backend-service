package edu.stanford.protege.webprotege.bulkop;


import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeGenerationContext;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.change.RemoveAxiomChange;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;

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
        var ontologyId = projectOntologies.getOntologyIds().filter(owlOntologyID -> owlOntologyID.getOntologyIRI().get()==entity.getIRI()).findAny();

        ontologyId.ifPresent(ontId -> {
            parents.forEach(parentClass -> {
                subClassAxiomIndex
                        .getSubClassOfAxiomsForSubClass(entity, ontId)
                        .filter(ax -> ax.getSuperClass().isNamed())
                        .filter(ax -> !ax.getSuperClass().equals(parentClass))
                        .forEach(ax -> processAxiom(ax, parentClass, ontId, changeList));
            });
        });


        return changeList.build(true);
    }

    private void processAxiom(OWLSubClassOfAxiom ax,
                              OWLClass parent,
                              OWLOntologyID ontId,
                              OntologyChangeList.Builder<Boolean> changeList) {
        var removeAxiom = RemoveAxiomChange.of(ontId, ax);
        changeList.add(removeAxiom);
        var replacementAx = dataFactory.getOWLSubClassOfAxiom(entity, parent, ax.getAnnotations());
        var addAxiom = AddAxiomChange.of(ontId, replacementAx);
        changeList.add(addAxiom);
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
