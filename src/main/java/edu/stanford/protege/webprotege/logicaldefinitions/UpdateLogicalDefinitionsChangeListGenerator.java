package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.frame.Mode;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.frame.translator.PropertyValue2AxiomTranslator;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UpdateLogicalDefinitionsChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final String commitMessage;

    private String adjustedCommitMessege = "";

    private final ChangeRequestId changeRequestId;

    private final LogicalConditions pristineLogicalConditions;

    private final LogicalConditions changedLogicalConditions;

    private final OWLClass subject;


    UpdateLogicalDefinitionsChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                                @Nonnull OWLDataFactory dataFactory,
                                                @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                LogicalConditions pristineLogicalConditions,
                                                LogicalConditions changedLogicalConditions,
                                                OWLClass subject,
                                                @Nonnull String commitMessage) {
        this.changeRequestId = changeRequestId;
        this.dataFactory = dataFactory;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.pristineLogicalConditions = pristineLogicalConditions;
        this.changedLogicalConditions = changedLogicalConditions;
        this.subject = subject;
        this.commitMessage = commitMessage;
    }


    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        var changeList = new OntologyChangeList.Builder<Boolean>();

        //This is assuming that there is only one ontology, maybe not ideal. May need to change in the future.
        OWLOntologyID ontId = projectOntologiesIndex.getOntologyIds().findFirst().get();

        generateChangesForLogicalDefinitions(changeList, ontId);
        generateChangesForNecessaryConditions(changeList, ontId);

        return changeList.build(true);
    }


    private void generateChangesForLogicalDefinitions(OntologyChangeList.Builder<Boolean> changeList, OWLOntologyID ontId) {
        LogicalDefinitionsDiff lcDiff = new LogicalDefinitionsDiff(pristineLogicalConditions.logicalDefinitions(), changedLogicalConditions.logicalDefinitions());
        lcDiff.executeDiff();

        lcDiff.getAddedStatements().stream()
                .map(addedLD -> addedToLCCommitMessage(addedLD, "Added Logical Definitions: "))
                .flatMap(ld -> getLogicalDefinitionAxioms(ld))
                .map(ax -> AddAxiomChange.of(ontId, ax))
                .forEach(chg -> changeList.add(chg));

        lcDiff.getRemovedStatements().stream()
                .map(removedLD -> addedToLCCommitMessage(removedLD, "Removed Logical Definitions: "))
                .flatMap(ld -> getLogicalDefinitionAxioms(ld))
                .map(ax -> RemoveAxiomChange.of(ontId, ax))
                .forEach(chg -> changeList.add(chg));
    }


    private void generateChangesForNecessaryConditions(OntologyChangeList.Builder<Boolean> changeList, OWLOntologyID ontId) {
        NecessaryConditionsDiff ncDiff = new NecessaryConditionsDiff(pristineLogicalConditions.necessaryConditions(), changedLogicalConditions.necessaryConditions());
        ncDiff.executeDiff();;

        PropertyValue2AxiomTranslator translator = new PropertyValue2AxiomTranslator();

        List<PropertyClassValue> addedStatements = ncDiff.getAddedStatements();

        ncDiff.getAddedStatements().stream()
                    .map(addedNC -> addedToNCCommitMessage(addedNC, "Added Necessary Conditions: "))
                    .flatMap(pcv -> translator.getAxioms(subject, pcv.toPlainPropertyValue(), Mode.MINIMAL).stream())
                    .map(ax -> AddAxiomChange.of(ontId, ax))
                    .forEach(chg -> changeList.add(chg));

        ncDiff.getRemovedStatements().stream()
                .map(removedNC -> addedToNCCommitMessage(removedNC, "Removed Necessary Conditions: "))
                .flatMap(pcv -> translator.getAxioms(subject, pcv.toPlainPropertyValue(), Mode.MINIMAL).stream())
                .map(ax -> RemoveAxiomChange.of(ontId, ax))
                .forEach(chg -> changeList.add(chg));
    }


    private LogicalDefinition addedToLCCommitMessage(LogicalDefinition addedLD, String addedTextCategory) {
        if (adjustedCommitMessege.contains(addedTextCategory) == false) {
            adjustedCommitMessege = adjustedCommitMessege + addedTextCategory;
        }

        adjustedCommitMessege = adjustedCommitMessege + addedLD.logicalDefinitionParent().getBrowserText() + ", ";

        for (PropertyClassValue axis2Filler : addedLD.axis2filler()) {
            adjustedCommitMessege = adjustedCommitMessege + "(" + axis2Filler.getProperty().getBrowserText() +
                    axis2Filler.getValue().getBrowserText() + ") ,";
        }

        adjustedCommitMessege = adjustedCommitMessege.substring(0, adjustedCommitMessege.length() - 2); //removed last ") ,"
        adjustedCommitMessege = adjustedCommitMessege + "; ";

        return addedLD;
    }


    private PropertyClassValue addedToNCCommitMessage(PropertyClassValue addedNC, String addedTextCategory) {
        if (adjustedCommitMessege.lastIndexOf(", ") == adjustedCommitMessege.length() - 3 ){
            adjustedCommitMessege = adjustedCommitMessege.substring(0, adjustedCommitMessege.length() - 2); //removed last ") ,"
        }

        if (adjustedCommitMessege.contains(addedTextCategory) == false) {
            adjustedCommitMessege = adjustedCommitMessege + addedTextCategory;
        }

        adjustedCommitMessege = adjustedCommitMessege + "(" + addedNC.getProperty().getBrowserText() +
                addedNC.getValue().getBrowserText() + "), ";

        return addedNC;
    }


    private Stream<OWLAxiom> getLogicalDefinitionAxioms(LogicalDefinition ld) {
        PropertyValue2AxiomTranslator translator = new PropertyValue2AxiomTranslator();

        List<OWLClassExpression> classExpressionList = getOWLClassExpressions(ld.axis2filler());
        classExpressionList.add(0, ld.logicalDefinitionParent().getEntity());

        OWLObjectIntersectionOf intersectionOf = dataFactory.getOWLObjectIntersectionOf(classExpressionList.toArray(new OWLClassExpression[0]));
        List<OWLAxiom> axiomList = new ArrayList<OWLAxiom>();
        axiomList.add(dataFactory.getOWLEquivalentClassesAxiom(subject, intersectionOf));

        return axiomList.stream();
    }

    private List<OWLClassExpression> getOWLClassExpressions(List<PropertyClassValue> axis2fillers) {
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
        if (adjustedCommitMessege.lastIndexOf(", ") == adjustedCommitMessege.length() - 3 ){
            adjustedCommitMessege = adjustedCommitMessege.substring(0, adjustedCommitMessege.length() - 2); //removed last ") ,"
        }

        if (commitMessage != null || commitMessage.isEmpty() == false) {
           adjustedCommitMessege = commitMessage + " ;" + adjustedCommitMessege;
        }

       return adjustedCommitMessege;
    }


    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }
}
