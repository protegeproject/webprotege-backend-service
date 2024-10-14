package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.frame.State;
import edu.stanford.protege.webprotege.index.EquivalentClassesAxiomsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogicalDefinitionExtractor {

    private static final Logger logger = LoggerFactory.getLogger(LogicalDefinitionExtractor.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;


    public LogicalDefinitionExtractor(@Nonnull ProjectId projectId,
                                      @Nonnull RenderingManager renderingManager,
                                      @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                      @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex
    ) {
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.equivalentClassesAxiomsIndex = equivalentClassesAxiomsIndex;
    }

    public List<LogicalDefinition> extractLogicalDefinitions(OWLClass subject) {
        Map<OWLClass, Map<OWLObjectProperty, OWLClass>> supercls2axis2fillerLogDefMap = new HashMap<OWLClass, Map<OWLObjectProperty, OWLClass>>();

        List<Map<OWLClass, Map<OWLObjectProperty, OWLClass>>> logicalDefinitions =
                projectOntologiesIndex.getOntologyIds()
                        .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject, ontId))
                        .flatMap(eca -> getSupercls2Axis2Filler(eca, subject))
                        .collect(Collectors.toList());

        return getTranslatedLogicalDefinitions(logicalDefinitions);
    }

    private Stream<Map<OWLClass, Map<OWLObjectProperty, OWLClass>>> getSupercls2Axis2Filler
            (@Nonnull OWLEquivalentClassesAxiom owlEquivalentClassesAxiom,
             OWLClass subject) {

        Map<OWLClass, Map<OWLObjectProperty, OWLClass>> supercls2axis2fillerLogDefMap = new HashMap<OWLClass, Map<OWLObjectProperty, OWLClass>>();

        List<OWLClassExpression> clsExpressions =
                owlEquivalentClassesAxiom.getClassExpressions().stream()
                        .filter(ce -> !ce.equals(subject))
                        .flatMap(this::asConjunctSet)
                        .collect(Collectors.toList());

        OWLClass parent = null;
        for (OWLClassExpression clsExp : clsExpressions) {

            logger.info("Found logical definition expression for " + subject + ": " + clsExp);

            if (clsExp instanceof OWLClass && ((OWLClass) clsExp).isNamed()) {
                if (parent != null) { //TODO: should we ignore the entire expression at this point? maybe.
                    logger.warn("Found logical definition with more than one parent: " + clsExp);
                }
                parent = ((OWLClass) clsExp);
            }

            Map<OWLObjectProperty, OWLClass> axis2FillerMap = getAxis2Filler(clsExp);

            if (parent != null && axis2FillerMap.isEmpty() == false) {
                supercls2axis2fillerLogDefMap.put(parent, axis2FillerMap);
            }
        }

        return Stream.of(supercls2axis2fillerLogDefMap);
    }

    private Map<OWLObjectProperty, OWLClass> getAxis2Filler(OWLClassExpression clsExp) {
        Map<OWLObjectProperty, OWLClass> axis2FillerMap = new HashMap<OWLObjectProperty, OWLClass>();

        if (clsExp instanceof OWLObjectSomeValuesFrom) {
            OWLObjectPropertyExpression prop = ((OWLObjectSomeValuesFrom) clsExp).getProperty();
            OWLClassExpression filler = ((OWLObjectSomeValuesFrom) clsExp).getFiller();

            if (prop instanceof OWLObjectProperty && filler instanceof OWLClass && filler.isNamed()) {
                axis2FillerMap.put((OWLObjectProperty) prop, (OWLClass) filler);
                logger.info("\tAdded axis 2 filler: " + prop + ": " + filler);
            } else {
                logger.warn("Found cls expression that does not fit the pattern. Ignoring: " + clsExp);
            }
        }

        return axis2FillerMap;
    }

    private Stream<OWLClassExpression> asConjunctSet(@Nonnull OWLClassExpression cls) {
        if (cls instanceof OWLObjectIntersectionOf) {
            return ((OWLObjectIntersectionOf) cls).getOperandsAsList()
                    .stream()
                    .flatMap(this::asConjunctSet);
        } else {
            return Stream.of(cls);
        }
    }

    private List<LogicalDefinition> getTranslatedLogicalDefinitions(List<Map<OWLClass, Map<OWLObjectProperty, OWLClass>>> logicalDefinitionsMap) {
        List<LogicalDefinition> logicalDefinitions = new ArrayList<LogicalDefinition>();

        for (Map<OWLClass, Map<OWLObjectProperty, OWLClass>> logDef : logicalDefinitionsMap) {

            List<PropertyClassValue> axis2FillerTranslated = new ArrayList<PropertyClassValue>();

            for (OWLClass parent : logDef.keySet()) {
                Map<OWLObjectProperty, OWLClass> axis2Fillers = logDef.get(parent);
                for (OWLObjectProperty axis : axis2Fillers.keySet()) {
                    OWLClass filler = axis2Fillers.get(axis);
                    PropertyClassValue pcv = PropertyClassValue.get(
                            renderingManager.getObjectPropertyData(axis),
                            renderingManager.getClassData(filler),
                            State.ASSERTED);
                    axis2FillerTranslated.add(pcv);
                }

                logicalDefinitions.add(new LogicalDefinition(renderingManager.getClassData(parent), axis2FillerTranslated));
            }
        }

        return logicalDefinitions;
    }

}
