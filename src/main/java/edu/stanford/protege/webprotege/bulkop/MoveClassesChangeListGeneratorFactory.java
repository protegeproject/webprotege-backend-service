package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class MoveClassesChangeListGeneratorFactory {

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    private final OWLDataFactory dataFactory;

    public MoveClassesChangeListGeneratorFactory(ProjectOntologiesIndex projectOntologiesIndex,
                                                 SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex,
                                                 OWLDataFactory dataFactory) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsBySubClassIndex = subClassOfAxiomsBySubClassIndex;
        this.dataFactory = dataFactory;
    }

    public MoveClassesChangeListGenerator create(ImmutableSet<OWLClass> clses,
                                                 OWLClass targetParent,
                                                 String commitMessage) {
        return new MoveClassesChangeListGenerator(clses,
                                                  targetParent,
                                                  commitMessage,
                                                  projectOntologiesIndex,
                                                  subClassOfAxiomsBySubClassIndex,
                                                  dataFactory);
    }
}
