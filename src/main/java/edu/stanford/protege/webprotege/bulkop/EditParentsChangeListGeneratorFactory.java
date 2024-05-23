package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class EditParentsChangeListGeneratorFactory {

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    private final OWLDataFactory dataFactory;

    public EditParentsChangeListGeneratorFactory(ProjectOntologiesIndex projectOntologiesIndex,
                                                 SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex,
                                                 OWLDataFactory dataFactory) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsBySubClassIndex = subClassOfAxiomsBySubClassIndex;
        this.dataFactory = dataFactory;
    }

    public EditParentsChangeListGenerator create(ChangeRequestId changeRequestId,
                                                 ImmutableSet<OWLClass> parents,
                                                 OWLClass entity,
                                                 String commitMessage) {
        return new EditParentsChangeListGenerator(changeRequestId, parents,
                                                  entity,
                                                  commitMessage,
                                                  projectOntologiesIndex,
                                                  subClassOfAxiomsBySubClassIndex,
                                                  dataFactory);
    }
}
