package edu.stanford.protege.webprotege.individuals;

import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.Page;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.entity.EntityNode;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.entity.OWLClassData;
import edu.stanford.protege.webprotege.index.IndividualsIndex;
import edu.stanford.protege.webprotege.index.IndividualsQueryResult;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsActionHandler extends AbstractProjectActionHandler<GetIndividualsAction, GetIndividualsResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetIndividualsActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final IndividualsIndex individualsIndex;

    @Inject
    public GetIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull EntityNodeRenderer entityNodeRenderer,
                                       @Nonnull IndividualsIndex individualsIndex) {
        super(accessManager);
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.entityNodeRenderer = entityNodeRenderer;
        this.individualsIndex = individualsIndex;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetIndividualsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetIndividualsResult execute(@Nonnull GetIndividualsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        OWLClass type = action.type();
        if(type == null) {
            type = DataFactory.getOWLThing();
        }
        IndividualsQueryResult result;
        String filterString = action.searchString();
        PageRequest pageRequest = action.pageRequest();
        result = individualsIndex.getIndividuals(type,
                                                 action.instanceRetrievalMode(),
                                                 filterString,
                                                 pageRequest);
        OWLClassData typeData = renderingManager.getClassData(type);
        logger.info("{} {} retrieved instances of {}",
                    projectId,
                    executionContext.userId(),
                    type);
        Page<OWLNamedIndividual> pg = result.getIndividuals();
        Page<EntityNode> entityNodes = pg.transform(entityNodeRenderer::render);
        return new GetIndividualsResult(Optional.of(typeData),
                                        entityNodes);
    }

    @Nonnull
    @Override
    public Class<GetIndividualsAction> getActionClass() {
        return GetIndividualsAction.class;
    }
}
