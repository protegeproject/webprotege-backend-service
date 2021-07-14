package edu.stanford.protege.webprotege.obo;

import edu.stanford.protege.webprotege.index.AxiomsByEntityReferenceIndex;
import edu.stanford.protege.webprotege.index.OntologyAnnotationsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class OBONamespaceCacheFactory {

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    public OBONamespaceCacheFactory(@Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                                    @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                    @Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                                    @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                    @Nonnull OWLDataFactory dataFactory) {
        this.ontologyAnnotationsIndex = ontologyAnnotationsIndex;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.axiomsByEntityReferenceIndex = axiomsByEntityReferenceIndex;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.dataFactory = dataFactory;
    }

    public OBONamespaceCache create() {
        return new OBONamespaceCache(ontologyAnnotationsIndex,
                                     defaultOntologyIdManager,
                                     axiomsByEntityReferenceIndex,
                                     projectOntologiesIndex,
                                     dataFactory);
    }
}
