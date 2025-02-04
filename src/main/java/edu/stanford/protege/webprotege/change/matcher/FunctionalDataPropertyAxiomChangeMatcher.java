package edu.stanford.protege.webprotege.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.description.SetDataPropertyFunctional;
import edu.stanford.protege.webprotege.change.description.UnsetDataPropertyFunctional;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class FunctionalDataPropertyAxiomChangeMatcher extends AbstractAxiomMatcher<OWLFunctionalDataPropertyAxiom> {

    @Inject
    public FunctionalDataPropertyAxiomChangeMatcher() {
        super(new TypeToken<OWLFunctionalDataPropertyAxiom>(){});
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLFunctionalDataPropertyAxiom axiom,
                                                                      List<OntologyChange> changes) {
        return Optional.of(ChangeSummary.get(SetDataPropertyFunctional.get(axiom.getProperty().asOWLDataProperty())));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLFunctionalDataPropertyAxiom axiom) {
        return Optional.of(ChangeSummary.get(UnsetDataPropertyFunctional.get(axiom.getProperty().asOWLDataProperty())));
    }
}
