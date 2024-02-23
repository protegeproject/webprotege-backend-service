package edu.stanford.protege.webprotege.match;

import com.google.common.base.Stopwatch;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.criteria.Criteria;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.common.PageCollector.toPage;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class GetMatchingEntitiesActionHandler extends AbstractProjectActionHandler<GetMatchingEntitiesAction, GetMatchingEntitiesResult>{

    private static final Logger logger = LoggerFactory.getLogger(GetMatchingEntitiesActionHandler.class);

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final EntityNodeRenderer nodeRenderer;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final MatchingEngine matchingEngine;

    @Inject
    public GetMatchingEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull DictionaryManager dictionaryManager,
                                            @Nonnull EntityNodeRenderer nodeRenderer,
                                            @Nonnull RenderingManager renderingManager, @Nonnull MatchingEngine matchingEngine) {
        super(accessManager);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.nodeRenderer = checkNotNull(nodeRenderer);
        this.renderingManager = renderingManager;
        this.matchingEngine = checkNotNull(matchingEngine);
    }

    @Nonnull
    @Override
    public Class<GetMatchingEntitiesAction> getActionClass() {
        return GetMatchingEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetMatchingEntitiesAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetMatchingEntitiesResult execute(@Nonnull GetMatchingEntitiesAction action, @Nonnull ExecutionContext executionContext) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        PageRequest pageRequest = action.pageRequest();
        Criteria criteria = action.criteria();
        Optional<Page<OWLEntityData>> result = matchingEngine.match(criteria)
                                                             .map(renderingManager::getRendering)
                                                             .sorted()
                                                             .collect(toPage(pageRequest.getPageNumber(),
                                                                           pageRequest.getPageSize()));
        stopwatch.stop();
        logger.info("{} {} Answered query in {} ms, matching {} entities",
                    action.projectId(),
                    executionContext.userId(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS),
                    result.map(Page::getTotalElements).orElse(0L));
        Optional<Page<EntityNode>> entityHierarchyNodes = result.map(pg -> {
            List<EntityNode> nodes = pg.getPageElements().stream()
                                       .map(ed -> nodeRenderer.render(ed.getEntity()))
                                       .collect(toList());
            return Page.create(pg.getPageNumber(),
                              pg.getPageCount(),
                              nodes,
                              pg.getTotalElements());
        });
        return entityHierarchyNodes.map(GetMatchingEntitiesResult::new)
                     .orElseGet(() -> new GetMatchingEntitiesResult(Page.emptyPage()));
    }
}
