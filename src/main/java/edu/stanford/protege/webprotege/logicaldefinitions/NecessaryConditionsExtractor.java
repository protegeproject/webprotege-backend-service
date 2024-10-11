package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.frame.PlainPropertyClassValue;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.frame.State;
import edu.stanford.protege.webprotege.index.EquivalentClassesAxiomsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
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

public class NecessaryConditionsExtractor {

    private static final Logger logger = LoggerFactory.getLogger(NecessaryConditionsExtractor.class);

    @Nonnull
    private final ProjectId projectId;
    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;


    public NecessaryConditionsExtractor(@Nonnull ProjectId projectId,
                                        @Nonnull RenderingManager renderingManager,
                                        @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                        @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex
    ) {
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsIndex = subClassOfAxiomsIndex;
    }

    public List<PropertyClassValue> extractNecessaryConditions(OWLClass subject) {

        List<Map<OWLObjectProperty, OWLClass>> necessaryConditions =
                projectOntologiesIndex.getOntologyIds()
                        .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(subject, ontId))
                        .map(OWLSubClassOfAxiom::getSuperClass)
                        .flatMap(this::asConjunctSet)
                        .flatMap(ce -> Stream.of(getAxis2Filler(ce)))
                        .collect(Collectors.toList());

        return getTranslatedNecessaryConditions(necessaryConditions);
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

    private List<PropertyClassValue> getTranslatedNecessaryConditions(List<Map<OWLObjectProperty, OWLClass>> necessaryConditionsList) {

        List<PropertyClassValue> axis2FillerTranslated = new ArrayList<PropertyClassValue>();

        for (Map<OWLObjectProperty, OWLClass> axis2Filler : necessaryConditionsList){

            for (OWLObjectProperty axis : axis2Filler.keySet()) {
                OWLClass filler = axis2Filler.get(axis);
                PropertyClassValue pcv = PropertyClassValue.get(
                        renderingManager.getObjectPropertyData(axis),
                        renderingManager.getClassData(filler),
                        State.ASSERTED);
                axis2FillerTranslated.add(pcv);
            }
        }

        return axis2FillerTranslated;
    }
}
