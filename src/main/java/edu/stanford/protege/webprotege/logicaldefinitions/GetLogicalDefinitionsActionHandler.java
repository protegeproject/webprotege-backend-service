package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.frame.PropertyClassValue;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GetLogicalDefinitionsActionHandler extends AbstractProjectActionHandler<GetLogicalDefinitionsAction, GetLogicalDefinitionsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetLogicalDefinitionsActionHandler.class);

    @Nonnull
    private final LogicalDefinitionExtractor logicalDefinitionExtractor;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final NecessaryConditionsExtractor necessaryConditionsExtractor;


    public GetLogicalDefinitionsActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull LogicalDefinitionExtractor logicalDefinitionExtractor,
                                              @Nonnull RenderingManager renderingManager, @Nonnull NecessaryConditionsExtractor necessaryConditionsExtractor
    ) {
        super(accessManager);
        this.logicalDefinitionExtractor = logicalDefinitionExtractor;
        this.renderingManager = renderingManager;
        this.necessaryConditionsExtractor = necessaryConditionsExtractor;
    }

    @NotNull
    @Override
    public Class<GetLogicalDefinitionsAction> getActionClass() {
        return GetLogicalDefinitionsAction.class;
    }

    @NotNull
    @Override
    public GetLogicalDefinitionsResponse execute(@NotNull GetLogicalDefinitionsAction action,
                                                 @NotNull ExecutionContext executionContext) {
        OWLClass subject = action.subject();

        List<LogicalDefinition> logicalDefinitions = logicalDefinitionExtractor.extractLogicalDefinitions(subject);
        List<PropertyClassValue> necessaryConditions = necessaryConditionsExtractor.extractNecessaryConditions(subject);
        OWLClassData classData = renderingManager.getClassData(subject);
        List<String> functionalAxioms = new ArrayList<>();
        try {
            functionalAxioms = LogicalConditionsSerializationToFunctionalSyntax.serializeLogicalDefinitions(logicalDefinitions, classData);
            functionalAxioms.addAll(LogicalConditionsSerializationToFunctionalSyntax.serializeNecessaryConditions(necessaryConditions, classData));
        } catch (OWLOntologyCreationException e) {
            logger.error("Error serializing functional axioms",e);
            throw new RuntimeException(e);
        }
        return new GetLogicalDefinitionsResponse(logicalDefinitions, necessaryConditions, functionalAxioms);
    }



}
