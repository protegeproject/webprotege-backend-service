package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.index.EquivalentClassesAxiomsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetLogicalDefinitionsActionHandler extends AbstractProjectActionHandler<GetLogicalDefinitionsRequest, GetLogicalDefinitionsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetLogicalDefinitionsActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;

    public GetLogicalDefinitionsActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull ProjectId projectId,
                                              @Nonnull RenderingManager renderingManager,
                                              @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                              @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                              @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex
    ) {

        super(accessManager);
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.equivalentClassesAxiomsIndex = equivalentClassesAxiomsIndex;
        this.subClassOfAxiomsIndex = subClassOfAxiomsIndex;
    }

    @NotNull
    @Override
    public Class<GetLogicalDefinitionsRequest> getActionClass() {
        return GetLogicalDefinitionsRequest.class;
    }

    @NotNull
    @Override
    public GetLogicalDefinitionsResponse execute(@NotNull GetLogicalDefinitionsRequest action,
                                                 @NotNull ExecutionContext executionContext) {
        OWLClass subject = action.subject();

        Map<OWLClass, Map<OWLObjectProperty, OWLClass>> supercls2axis2fillerLogDefMap = new HashMap<OWLClass, Map<OWLObjectProperty, OWLClass>>();

        List<Map<OWLClass, Map<OWLObjectProperty, OWLClass>>> logicalDefinitions =
                projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject, ontId))
                .flatMap(eca -> getSupercls2Axis2Filler(eca, subject))
                .collect(Collectors.toList());


        List<Map<OWLObjectProperty, OWLClass>> necessaryConditions =
                projectOntologiesIndex.getOntologyIds()
                        .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(subject, ontId))
                        .map(OWLSubClassOfAxiom::getSuperClass)
                        .flatMap(this::asConjunctSet)
                        .flatMap(ce -> Stream.of(getAxis2Filler(ce)))
                        .collect(Collectors.toList());

        return new GetLogicalDefinitionsResponse(getTranslatedLogicalDefinitons(logicalDefinitions),
                                                getTranslatedNecessaryConditions(necessaryConditions));
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
}
