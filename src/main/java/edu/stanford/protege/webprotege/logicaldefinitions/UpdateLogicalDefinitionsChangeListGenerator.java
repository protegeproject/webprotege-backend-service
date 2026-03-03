package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.Mode;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.frame.translator.PropertyValue2AxiomTranslator;
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

        if (!ncDiff.getAddedStatements().isEmpty() || !ncDiff.getRemovedStatements().isEmpty()) {
            final OWLDataFactory df = DataFactory.get();
            PropertyValue2AxiomTranslator translator = new PropertyValue2AxiomTranslator();

            projectOntologiesIndex.getOntologyIds().forEach(ontologyId -> {
                List<OWLClassExpression> classExpressionList = getOWLClassExpressions(new HashSet<>(pristineLogicalConditions.necessaryConditions()));
                var intersectionOfRemovedConditions = dataFactory.getOWLObjectIntersectionOf(new HashSet<>(classExpressionList));
                changeList.add(RemoveAxiomChange.of(ontologyId, df.getOWLSubClassOfAxiom(subject, intersectionOfRemovedConditions)));

                pristineLogicalConditions.necessaryConditions().stream()
                        .flatMap(pcv -> translator.getAxioms(subject, pcv.toPlainPropertyValue(), Mode.MINIMAL).stream())
                        .map(ax -> RemoveAxiomChange.of(ontId, ax))
                        .forEach(changeList::add);

            });

            List<OWLClassExpression> classExpressionList = getOWLClassExpressions(new HashSet<>(changedLogicalConditions.necessaryConditions()));
            if (!classExpressionList.isEmpty()) {
                var intersectionOfAddedConditions = dataFactory.getOWLObjectIntersectionOf(new HashSet<>(classExpressionList));
                changeList.add(AddAxiomChange.of(ontId, df.getOWLSubClassOfAxiom(subject,  intersectionOfAddedConditions)));
            }
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
                    changeList.addAll(getLogicalDefinitionAxioms(logicalDefinition.logicalDefinitionParent(), logicalDefinition.axis2filler())
                            .stream().map(ax -> RemoveAxiomChange.of(ontId, ax)).collect(Collectors.toList()));

                    changeList.add(RemoveAxiomChange.of(ontologyId,equivalentClassAxiom));
                });

            }
            for(LogicalDefinition logicalDefinition : changedLogicalConditions.logicalDefinitions()) {
                List<OWLClassExpression> classExpressionList = getOWLClassExpressions(new HashSet<>(logicalDefinition.axis2filler()));
                if(!classExpressionList.isEmpty()) {
                    Set<OWLClassExpression> intersectionOperands = new HashSet<>();

                    intersectionOperands.add(logicalDefinition.logicalDefinitionParent().getEntity());
                    intersectionOperands.addAll(classExpressionList);
                    var intersectionOfAddedConditions = dataFactory.getOWLObjectIntersectionOf(intersectionOperands);

                    var equivalentClassAxiom =  df.getOWLEquivalentClassesAxiom(subject, intersectionOfAddedConditions);
                    changeList.add(AddAxiomChange.of(ontId, equivalentClassAxiom));
                }
            }
        }

    }
    private List<OWLAxiom> getLogicalDefinitionAxioms(OWLClassData logicalDefinitionParent, List<PropertyClassValue> axis2Filler) {

        List<OWLClassExpression> classExpressionList = getOWLClassExpressions(new HashSet<>(axis2Filler));
        classExpressionList.add(0, logicalDefinitionParent.getEntity());

        OWLObjectIntersectionOf intersectionOf = dataFactory.getOWLObjectIntersectionOf(classExpressionList.toArray(new OWLClassExpression[0]));
        List<OWLAxiom> axiomList = new ArrayList<OWLAxiom>();
        axiomList.add(dataFactory.getOWLEquivalentClassesAxiom(subject, intersectionOf));

        return axiomList;
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
