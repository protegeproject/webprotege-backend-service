package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.LinearizationManager;
import edu.stanford.protege.webprotege.postcoordination.PostcoordinationManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.*;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractProjectChangeHandler<Set<OWLClass>, CreateClassesAction, CreateClassesResult> {

    private final Logger logger = LoggerFactory.getLogger(CreateClassesActionHandler.class);

    @Nonnull
    private final CreateClassesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final LinearizationManager linearizationManager;

    @Nonnull
    private final PostcoordinationManager postcoordinationManager;

    @Inject
    public CreateClassesActionHandler(@Nonnull AccessManager accessManager,

                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull CreateClassesChangeGeneratorFactory changeFactory,
                                      @Nonnull EntityNodeRenderer entityNodeRenderer,
                                      @Nonnull LinearizationManager linearizationManager,
                                      @Nonnull PostcoordinationManager postcoordinationManager) {
        super(accessManager, applyChanges);
        this.changeGeneratorFactory = checkNotNull(changeFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
        this.linearizationManager = linearizationManager;
        this.postcoordinationManager = postcoordinationManager;
    }

    @Nonnull
    @Override
    public Class<CreateClassesAction> getActionClass() {
        return CreateClassesAction.class;
    }

    @Nonnull
    @Override
    protected List<BuiltInAction> getRequiredExecutableBuiltInActions(CreateClassesAction action) {
        return asList(CREATE_CLASS, EDIT_ONTOLOGY);
    }


    @Override
    protected ChangeListGenerator<Set<OWLClass>> getChangeListGenerator(CreateClassesAction action, ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.sourceText(),
                action.langTag(),
                action.parents(),
                action.changeRequestId());
    }

    @Override
    protected CreateClassesResult createActionResult(ChangeApplicationResult<Set<OWLClass>> changeApplicationResult,
                                                     CreateClassesAction action,
                                                     ExecutionContext executionContext) {
        Set<OWLClass> classes = changeApplicationResult.getSubject();
        classes.forEach(newClass ->
                {
                    try {
                        linearizationManager.createLinearizationFromParent(
                                newClass.getIRI(),
                                /*
                                ToDo:
                                  While creating a class the action.parents() set contains only one element: The direct parent of the new created entity.
                                  Check with team to see when we have multiple parents there and how to handle it.
                                 */
                                action.parents().stream().findFirst().get().getIRI(),
                                action.projectId(),
                                executionContext
                        ).get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("MergeLinearizationsError: " + e);
                    }
                    try {
                        postcoordinationManager.createPostcoordinationFromParent(
                                newClass.getIRI(),
                                /*
                                ToDo:
                                  While creating a class the action.parents() set contains only one element: The direct parent of the new created entity.
                                  Check with team to see when we have multiple parents there and how to handle it.
                                 */
                                action.parents().stream().findFirst().get().getIRI(),
                                action.projectId(),
                                executionContext
                        ).get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error("CreatePostcoordinationError: " + e);
                    }
                }
        );
        return new CreateClassesResult(
                action.changeRequestId(),
                action.projectId(),
                classes.stream().map(entityNodeRenderer::render).collect(toImmutableSet())
        );
    }
}
