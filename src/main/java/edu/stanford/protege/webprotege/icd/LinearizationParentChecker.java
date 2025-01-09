package edu.stanford.protege.webprotege.icd;

import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Set;

@Component
public class LinearizationParentChecker {

    @Nonnull
    private final LinearizationManager linearizationManager;

    public LinearizationParentChecker(LinearizationManager linearizationManager) {
        this.linearizationManager = linearizationManager;
    }

    public boolean hasLinearizationParent(OWLClass owlClas, Set<OWLClass> parentClasses) {

    }
}
