package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.frame.PlainPropertyClassValue;
import edu.stanford.protege.webprotege.index.EquivalentClassesAxiomsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.apache.lucene.index.DocIDMerger;
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
    public Class<GetLogicalDefinitionsRequest> getActionClass() {
        return GetLogicalDefinitionsRequest.class;
    }

    @NotNull
    @Override
    public GetLogicalDefinitionsResponse execute(@NotNull GetLogicalDefinitionsRequest action,
                                                 @NotNull ExecutionContext executionContext) {
        OWLClass subject = action.subject();

        return new GetLogicalDefinitionsResponse(logicalDefinitionExtractor.extractLogicalDefinitions(subject),
                                                necessaryConditionsExtractor.extractNecessaryConditions(subject));
    }



}
