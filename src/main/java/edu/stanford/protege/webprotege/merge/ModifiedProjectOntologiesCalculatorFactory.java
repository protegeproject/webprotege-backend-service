package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.project.Ontology;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class ModifiedProjectOntologiesCalculatorFactory {

    private OntologyDiffCalculator ontologyDiffCalculator;

    public ModifiedProjectOntologiesCalculatorFactory(OntologyDiffCalculator ontologyDiffCalculator) {
        this.ontologyDiffCalculator = ontologyDiffCalculator;
    }

    public ModifiedProjectOntologiesCalculator create(Collection<Ontology> projectOntologies,
                                                      Collection<Ontology> uploadedOntologies) {
        return new ModifiedProjectOntologiesCalculator(projectOntologies,
                                                       uploadedOntologies,
                                                       ontologyDiffCalculator);
    }
}
