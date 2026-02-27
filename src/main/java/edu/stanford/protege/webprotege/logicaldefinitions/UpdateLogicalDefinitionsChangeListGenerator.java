package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdateLogicalDefinitionsChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final String commitMessage;

    private final ChangeRequestId changeRequestId;

    private final LogicalConditions pristineLogicalConditions;

    private final LogicalConditions changedLogicalConditions;

    private final OWLClass subject;

    private final DefaultOntologyIdManager defaultOntologyIdManager;


    UpdateLogicalDefinitionsChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                                @Nonnull OWLDataFactory dataFactory,
                                                @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                LogicalConditions pristineLogicalConditions,
                                                LogicalConditions changedLogicalConditions,
                                                OWLClass subject,
                                                @Nonnull String commitMessage,
                                                DefaultOntologyIdManager defaultOntologyIdManager) {
        this.changeRequestId = changeRequestId;
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.pristineLogicalConditions = pristineLogicalConditions;
        this.changedLogicalConditions = changedLogicalConditions;
        this.subject = subject;
        this.commitMessage = commitMessage;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }


    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        var changeList = new OntologyChangeList.Builder<Boolean>();

        OWLOntologyID ontId = defaultOntologyIdManager.getDefaultOntologyId();

        generateChangesForLogicalDefinitions(changeList, ontId);
        generateChangesForNecessaryConditions(changeList, ontId);

        return changeList.build(!changeList.isEmpty());
    }

    private void generateChangesForNecessaryConditions(OntologyChangeList.Builder<Boolean> changeList, OWLOntologyID ontId) {
        NecessaryConditionsDiff ncDiff = new NecessaryConditionsDiff(pristineLogicalConditions.necessaryConditions(), changedLogicalConditions.necessaryConditions());
        ncDiff.executeDiff();
        final OWLDataFactory df = DataFactory.get();

        projectOntologiesIndex.getOntologyIds().forEach(ontologyId -> {
            var intersectionOfRemovedConditions = dataFactory.getOWLObjectIntersectionOf(new HashSet<>(getOWLClassExpressions(new HashSet<>(pristineLogicalConditions.necessaryConditions()))));
            changeList.add(RemoveAxiomChange.of(ontologyId, df.getOWLSubClassOfAxiom(subject, intersectionOfRemovedConditions)));
        });

        if (!ncDiff.getAddedStatements().isEmpty() || !ncDiff.getRemovedStatements().isEmpty()) {
            var intersectionOfAddedConditions = dataFactory.getOWLObjectIntersectionOf(new HashSet<>(getOWLClassExpressions(new HashSet<>(changedLogicalConditions.necessaryConditions()))));
            changeList.add(AddAxiomChange.of(ontId, df.getOWLSubClassOfAxiom(subject,  intersectionOfAddedConditions)));
        }


    }

    private void generateChangesForLogicalDefinitions(OntologyChangeList.Builder<Boolean> changeList, OWLOntologyID ontId) {
        LogicalDefinitionsDiff lcDiff = new LogicalDefinitionsDiff(pristineLogicalConditions.logicalDefinitions(), changedLogicalConditions.logicalDefinitions());
        lcDiff.executeDiff();
        if(!lcDiff.getAddedStatementsMap().isEmpty() || !lcDiff.getRemovedStatementsMap().isEmpty()) {
            final OWLDataFactory df = DataFactory.get();

            for(LogicalDefinition logicalDefinition : pristineLogicalConditions.logicalDefinitions()) {
                Set<OWLClassExpression> intersectionOperands = new HashSet<>();

                intersectionOperands.add(logicalDefinition.logicalDefinitionParent().getEntity());
                intersectionOperands.addAll(getOWLClassExpressions(new HashSet<>(logicalDefinition.axis2filler())));
                var intersectionOfAddedConditions = dataFactory.getOWLObjectIntersectionOf(intersectionOperands);

                var equivalentClassAxiom =  df.getOWLEquivalentClassesAxiom(subject, intersectionOfAddedConditions);
                projectOntologiesIndex.getOntologyIds().forEach(ontologyId -> {
                    changeList.add(RemoveAxiomChange.of(ontologyId,equivalentClassAxiom));
                });

            }
            for(LogicalDefinition logicalDefinition : changedLogicalConditions.logicalDefinitions()) {
                Set<OWLClassExpression> intersectionOperands = new HashSet<>();

                intersectionOperands.add(logicalDefinition.logicalDefinitionParent().getEntity());
                intersectionOperands.addAll(getOWLClassExpressions(new HashSet<>(logicalDefinition.axis2filler())));
                var intersectionOfAddedConditions = dataFactory.getOWLObjectIntersectionOf(intersectionOperands);

                var equivalentClassAxiom =  df.getOWLEquivalentClassesAxiom(subject, intersectionOfAddedConditions);
                changeList.add(AddAxiomChange.of(ontId, equivalentClassAxiom));

            }
        }

    }


    private List<OWLClassExpression> getOWLClassExpressions(Set<PropertyClassValue> axis2fillers) {
        return axis2fillers.stream()
                .map(pcv -> {
                    return dataFactory.getOWLObjectSomeValuesFrom(pcv.getProperty().getObject(), pcv.getValue().getEntity());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @NotNull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"cursor : pointer;\"")
                .append(" onclick=\"window.focusClickedEntity && window.focusClickedEntity(event, '")
                .append(subject.getIRI())
                .append("')\"")
                .append(" title=\"Click to select entity ")
                .append(subject.getIRI()).append("\">");
        sb.append(commitMessage);
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }
}
