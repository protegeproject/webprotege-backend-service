package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.frame.State;
import edu.stanford.protege.webprotege.index.EquivalentClassesAxiomsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import kotlin.Pair;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
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
        Map<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>> supercls2axis2fillerLogDefMap = new HashMap<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>>();

        List<Map<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>>> logicalDefinitions =
                projectOntologiesIndex.getOntologyIds()
                        .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject, ontId))
                        .flatMap(eca -> getSupercls2Axis2Filler(eca, subject))
                        .collect(Collectors.toList());

        return getTranslatedLogicalDefinitions(logicalDefinitions);
    }

    private Stream<Map<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>>> getSupercls2Axis2Filler
            (@Nonnull OWLEquivalentClassesAxiom owlEquivalentClassesAxiom,
             OWLClass subject) {

        Map<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>> supercls2axis2fillerLogDefMap = new HashMap<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>>();

        List<OWLClassExpression> clsExpressions =
                owlEquivalentClassesAxiom.getClassExpressions().stream()
                        .filter(ce -> !ce.equals(subject))
                        .flatMap(this::asConjunctSet)
                        .collect(Collectors.toList());

        OWLClass parent = null;
        List<Pair<OWLObjectProperty, OWLClass>> axis2FillerList = new ArrayList<Pair<OWLObjectProperty, OWLClass>>();

        for (OWLClassExpression clsExp : clsExpressions) {

            logger.info("Found logical definition expression for " + subject + ": " + clsExp);

            if (clsExp instanceof OWLClass && ((OWLClass) clsExp).isNamed()) {
                if (parent != null) { //TODO: should we ignore the entire expression at this point? maybe.
                    logger.warn("Found logical definition with more than one parent: " + clsExp);
                }
                parent = ((OWLClass) clsExp);
            }

            Optional<Pair<OWLObjectProperty, OWLClass>> axis2Filler = getAxis2Filler(clsExp);

            if (axis2Filler.isEmpty() == false) {
                axis2FillerList.add(axis2Filler.get());
            }
        }

        if (parent != null && axis2FillerList.isEmpty() == false) {
            supercls2axis2fillerLogDefMap.put(parent, axis2FillerList);
        }

        return Stream.of(supercls2axis2fillerLogDefMap);
    }

    private Optional<Pair<OWLObjectProperty, OWLClass>> getAxis2Filler(OWLClassExpression clsExp) {
        if (clsExp instanceof OWLObjectSomeValuesFrom) {
            OWLObjectPropertyExpression prop = ((OWLObjectSomeValuesFrom) clsExp).getProperty();
            OWLClassExpression filler = ((OWLObjectSomeValuesFrom) clsExp).getFiller();

            if (prop instanceof OWLObjectProperty && filler instanceof OWLClass && filler.isNamed()) {
                logger.info("\tAdded axis 2 filler: " + prop + ": " + filler);
                return Optional.of(new Pair((OWLObjectProperty) prop, (OWLClass) filler));

            } else {
                logger.warn("Found cls expression that does not fit the pattern. Ignoring: " + clsExp);
            }
        }

        return Optional.empty();
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

    private List<LogicalDefinition> getTranslatedLogicalDefinitions(List<Map<OWLClass,
            List<Pair<OWLObjectProperty, OWLClass>>>> logicalDefinitionsList) {
        List<LogicalDefinition> logicalDefinitions = new ArrayList<LogicalDefinition>();

        for (Map<OWLClass, List<Pair<OWLObjectProperty, OWLClass>>> logDef : logicalDefinitionsList) {

            List<PropertyClassValue> axis2FillerTranslated = new ArrayList<PropertyClassValue>();

            for (OWLClass parent : logDef.keySet()) {
                List<Pair<OWLObjectProperty, OWLClass>> axis2Fillers = logDef.get(parent);
                for (Pair<OWLObjectProperty, OWLClass> axis2FillerPair : axis2Fillers) {
                    OWLObjectProperty axis = axis2FillerPair.getFirst();
                    OWLClass filler = axis2FillerPair.getSecond();
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
