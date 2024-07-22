package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.*;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.linearization.CreateLinearizationManager;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.*;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractProjectChangeHandler<Set<OWLClass>, CreateClassesAction, CreateClassesResult> {

    @Nonnull
    private final CreateClassesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final CreateLinearizationManager createLinearizationManager;

    @Inject
    public CreateClassesActionHandler(@Nonnull AccessManager accessManager,

                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull CreateClassesChangeGeneratorFactory changeFactory,
                                      @Nonnull EntityNodeRenderer entityNodeRenderer, @Nonnull CreateLinearizationManager createLinearizationManager) {
        super(accessManager, applyChanges);
        this.changeGeneratorFactory = checkNotNull(changeFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
        this.createLinearizationManager = createLinearizationManager;
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
        classes.forEach(newClass -> createLinearizationManager.createLinearizationFromParent(
                        newClass.getIRI(),
                        action.parents().stream().findFirst().get().getIRI()
                )
        );
        return new CreateClassesResult(
                action.changeRequestId(),
                action.projectId(),
                classes.stream().map(entityNodeRenderer::render).collect(toImmutableSet())
        );
    }
}
