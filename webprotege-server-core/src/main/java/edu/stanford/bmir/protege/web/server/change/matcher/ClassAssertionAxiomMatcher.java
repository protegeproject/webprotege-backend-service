package edu.stanford.bmir.protege.web.server.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class ClassAssertionAxiomMatcher extends AbstractAxiomMatcher<OWLClassAssertionAxiom> {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public ClassAssertionAxiomMatcher(OWLObjectStringFormatter formatter) {
        super(new TypeToken<OWLClassAssertionAxiom>(){});
        this.formatter = formatter;
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLClassAssertionAxiom axiom,
                                                                      List<OWLOntologyChangeData> changes) {
        var possibleEntityCreation = changes.stream()
                .filter(data -> data instanceof AddAxiomData)
                .map(data -> ((AddAxiomData) data).getAxiom())
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> ((OWLDeclarationAxiom) ax).getEntity())
                .anyMatch(entity -> entity.equals(axiom.getIndividual()));
        if(possibleEntityCreation) {
            return Optional.empty();
        }
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getIndividual(),
                                                           axiom.getClassExpression());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            var msg = formatter.formatString("Added relationship (%s %s) on %s", property.get(), filler.get(), axiom.getIndividual());
            return Optional.of(ChangeSummary.get(msg));
        }
        else {
            var msg = formatter.formatString("Added %s as a type to %s", axiom.getClassExpression(), axiom.getIndividual());
            return Optional.of(ChangeSummary.get(msg));
        }
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLClassAssertionAxiom axiom) {
        PropertyFiller propertyFiller = new PropertyFiller(axiom.getIndividual(),
                                                           axiom.getClassExpression());
        Optional<OWLProperty> property = propertyFiller.getProperty();
        Optional<OWLObject> filler = propertyFiller.getFiller();
        if(property.isPresent() && filler.isPresent()) {
            var msg = formatter.formatString("Removed relationship (%s %s) on %s", property.get(), filler.get(), axiom.getIndividual());
            return Optional.of(ChangeSummary.get(msg));
        }
        else {
            var msg = formatter.formatString("Removed %s as a type from %s", axiom.getClassExpression(), axiom.getIndividual());
            return Optional.of(ChangeSummary.get(msg));
        }
    }

    @Override
    protected boolean allowSignatureDeclarations() {
        return true;
    }
}
