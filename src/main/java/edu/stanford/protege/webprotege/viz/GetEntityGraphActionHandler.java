package edu.stanford.protege.webprotege.viz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class GetEntityGraphActionHandler extends AbstractProjectActionHandler<GetEntityGraphAction, GetEntityGraphResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetEntityGraphActionHandler.class);

    @Nonnull
    private final EntityGraphBuilderFactory graphBuilderFactory;

    @Nonnull
    private final EdgeMatcherFactory edgeMatcherFactory;

    @Nonnull
    private final EntityGraphSettingsRepository entityGraphSettingsRepository;

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public GetEntityGraphActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EntityGraphBuilderFactory graphBuilderFactory,
                                       @Nonnull EdgeMatcherFactory edgeMatcherFactory,
                                       @Nonnull EntityGraphSettingsRepository entityGraphSettingsRepository,
                                       @Nonnull ObjectMapper objectMapper) {
        super(accessManager);
        this.graphBuilderFactory = checkNotNull(graphBuilderFactory);
        this.edgeMatcherFactory = checkNotNull(edgeMatcherFactory);
        this.entityGraphSettingsRepository = checkNotNull(entityGraphSettingsRepository);
        this.objectMapper = objectMapper;
    }

    @Nonnull
    @Override
    public Class<GetEntityGraphAction> getActionClass() {
        return GetEntityGraphAction.class;
    }

    @Nonnull
    @Override
    public GetEntityGraphResult execute(@Nonnull GetEntityGraphAction action, @Nonnull ExecutionContext executionContext) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        var projectId = action.projectId();
        var userId = executionContext.userId();
        logger.info("Getting entity graph settings for " + userId);
        var projectUserSettings = entityGraphSettingsRepository.getSettingsForUserOrProjectDefault(projectId, userId);
        var entityGraphSettings = projectUserSettings.getSettings();
        var criteria = projectUserSettings.getSettings()
                                          .getCombinedActiveFilterCriteria();
        var edgeMatcher = edgeMatcherFactory.createMatcher(criteria.simplify());
        try {
            logger.info("Criteria: " + objectMapper.writeValueAsString(criteria));
            logger.info("Simplified Criteria: " + objectMapper.writeValueAsString(criteria.simplify()));
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }

        var graph = graphBuilderFactory.create(edgeMatcher)
                                       .createGraph(action.entity());
        stopwatch.stop();
        logger.debug("Created entity graph [{} nodes; edges {}] in {} ms",
                    graph.getNodes().size(),
                    graph.getEdges().size(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return new GetEntityGraphResult(graph, entityGraphSettings);
    }
}
