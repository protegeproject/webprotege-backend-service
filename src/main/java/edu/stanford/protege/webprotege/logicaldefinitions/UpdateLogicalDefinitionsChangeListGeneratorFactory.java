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

    private final LogicalConditions pristineLogicalConditions;

    private final LogicalConditions changedLogicalConditions;

    private final OWLClass subject;

    private final String commitMessage;

    public UpdateLogicalDefinitionsChangeListGeneratorFactory(
                            ProjectOntologiesIndex projectOntologiesIndex,
                            OWLDataFactory dataFactory,
                            LogicalConditions pristineLogicalConditions,
                            LogicalConditions changedLogicalConditions,
                            OWLClass subject,
                            String commitMessage) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.dataFactory = dataFactory;
        this.pristineLogicalConditions = pristineLogicalConditions;
        this.changedLogicalConditions = changedLogicalConditions;
        this.subject = subject;
        this.commitMessage = commitMessage;
    }


    public ChangeListGenerator<Boolean> create(ChangeRequestId changeRequestId,
                                               ProjectId projectId,
                                               OWLClass subject,
                                               LogicalConditions pristineLogicalConditions,
                                               LogicalConditions changedLogicalConditions) {
        return  new UpdateLogicalDefinitionsChangeListGenerator(changeRequestId, dataFactory, projectOntologiesIndex,
               pristineLogicalConditions, changedLogicalConditions, subject, commitMessage);

    }
}
