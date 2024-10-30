package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class GetLogicalDefinitionsActionHandler extends AbstractProjectActionHandler<GetLogicalDefinitionsAction, GetLogicalDefinitionsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetLogicalDefinitionsActionHandler.class);

    @Nonnull
    private final LogicalDefinitionExtractor logicalDefinitionExtractor;

    @Nonnull
    private final NecessaryConditionsExtractor necessaryConditionsExtractor;


    public GetLogicalDefinitionsActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull LogicalDefinitionExtractor logicalDefinitionExtractor,
                                              @Nonnull NecessaryConditionsExtractor necessaryConditionsExtractor
    ) {
        super(accessManager);
        this.logicalDefinitionExtractor = logicalDefinitionExtractor;
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

        return new GetLogicalDefinitionsResponse(logicalDefinitionExtractor.extractLogicalDefinitions(subject),
                                                necessaryConditionsExtractor.extractNecessaryConditions(subject));
    }



}
