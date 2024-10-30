package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

public class UpdateLogicalDefinitionsChangeListGeneratorFactory {

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final OWLDataFactory dataFactory;


    public UpdateLogicalDefinitionsChangeListGeneratorFactory(
                            ProjectOntologiesIndex projectOntologiesIndex,
                            OWLDataFactory dataFactory) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.dataFactory = dataFactory;
    }


    public ChangeListGenerator<Boolean> create(ChangeRequestId changeRequestId,
                                               ProjectId projectId,
                                               OWLClass subject,
                                               String commitMessage,
                                               LogicalConditions pristineLogicalConditions,
                                               LogicalConditions changedLogicalConditions) {
        return  new UpdateLogicalDefinitionsChangeListGenerator(changeRequestId, dataFactory, projectOntologiesIndex,
               pristineLogicalConditions, changedLogicalConditions, subject, commitMessage);

    }
}
