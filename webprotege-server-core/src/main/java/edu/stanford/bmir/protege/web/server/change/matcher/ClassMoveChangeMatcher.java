package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/04/16
 */
public class ClassMoveChangeMatcher implements ChangeMatcher {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public ClassMoveChangeMatcher(OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<ChangeSummary> getDescription(List<OWLOntologyChangeData> changeData) {
        if(changeData.size() != 2) {
            return Optional.empty();
        }
        OWLOntologyChangeData change0 = changeData.get(0);
        OWLOntologyChangeData change1 = changeData.get(1);
        CandidateAxiomEdit<OWLSubClassOfAxiom> edit = new CandidateAxiomEdit<>(change0, change1, AxiomType.SUBCLASS_OF);
        if (!edit.hasAddAndRemove()) {
            return Optional.empty();
        }
        OWLSubClassOfAxiom addAx = edit.getAddAxiom().get();
        OWLSubClassOfAxiom remAx = edit.getRemoveAxiom().get();
        if(!addAx.getSubClass().equals(remAx.getSubClass())) {
            return Optional.empty();
        }
        if(addAx.getSuperClass().isAnonymous()) {
            return Optional.empty();
        }
        if(remAx.getSuperClass().isAnonymous()) {
            return Optional.empty();
        }
        var msg = formatter.formatString("Moved class %s from %s to %s", addAx.getSubClass(), remAx.getSuperClass(), addAx.getSuperClass());
        return Optional.of(ChangeSummary.get(msg));
    }
}
