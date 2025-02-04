package edu.stanford.protege.webprotege.change.matcher;

import com.google.common.reflect.TypeToken;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.description.AddedRelationship;
import edu.stanford.protege.webprotege.change.description.RemovedRelationship;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Mar 2017
 */
public class PropertyAssertionAxiomMatcher extends AbstractAxiomMatcher<OWLPropertyAssertionAxiom<?,?>> {

    @Inject
    public PropertyAssertionAxiomMatcher() {
        super(new TypeToken<OWLPropertyAssertionAxiom<?,?>>() {});
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForAddAxiomChange(OWLPropertyAssertionAxiom<?, ?> axiom,
                                                                      List<OntologyChange> changes) {
        return Optional.of(ChangeSummary.get(AddedRelationship.get(axiom.getSubject(),
                                                                   (OWLProperty) axiom.getProperty(),
                                                                   axiom.getObject())));
    }

    @Override
    protected Optional<ChangeSummary> getDescriptionForRemoveAxiomChange(OWLPropertyAssertionAxiom<?,?> axiom) {
        return Optional.of(ChangeSummary.get(RemovedRelationship.get(axiom.getSubject(),
                                                                     (OWLProperty) axiom.getProperty(),
                                                                     axiom.getObject())));
    }

    @Override
    protected boolean allowSignatureDeclarations() {
        return true;
    }
}
