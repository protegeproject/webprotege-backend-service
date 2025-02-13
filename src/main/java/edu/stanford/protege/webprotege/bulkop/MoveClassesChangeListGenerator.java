package edu.stanford.protege.webprotege.bulkop;



import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveClassesChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final ChangeRequestId changeRequestId;

    @Nonnull
    private final ImmutableSet<OWLClass> childClasses;

    @Nonnull
    private final OWLClass targetParent;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologies;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassAxiomIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final String commitMessage;

    @Inject
    public MoveClassesChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                          @Nonnull ImmutableSet<OWLClass> childClasses,
                                          @Nonnull OWLClass targetParent,
                                          @Nonnull String commitMessage,
                                          @Nonnull ProjectOntologiesIndex projectOntologies,
                                          @Nonnull SubClassOfAxiomsBySubClassIndex subClassAxiomIndex,
                                          @Nonnull OWLDataFactory dataFactory) {
        this.changeRequestId = changeRequestId;
        this.childClasses = checkNotNull(childClasses);
        this.targetParent = checkNotNull(targetParent);
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
            childClasses.forEach(childClass -> {
                subClassAxiomIndex
                        .getSubClassOfAxiomsForSubClass(childClass, ontId)
                        .filter(ax -> ax.getSuperClass().isNamed())
                        .filter(ax -> !ax.getSuperClass().equals(targetParent))
                        .forEach(ax -> processAxiom(ax, childClass, ontId, changeList));
            });
        });
        return changeList.build(true);
    }

    private void processAxiom(OWLSubClassOfAxiom ax,
                              OWLClass childCls,
                              OWLOntologyID ontId,
                              OntologyChangeList.Builder<Boolean> changeList) {
        var removeAxiom = RemoveAxiomChange.of(ontId, ax);
        changeList.add(removeAxiom);
        var replacementAx = dataFactory.getOWLSubClassOfAxiom(childCls, targetParent, ax.getAnnotations());
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
