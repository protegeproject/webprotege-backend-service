package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.CreateClassesChangeGeneratorFactory;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import edu.stanford.protege.webprotege.entity.CreateClassesAction;
import edu.stanford.protege.webprotege.entity.CreateClassesResult;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_CLASS;
import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractProjectChangeHandler<Set<OWLClass>, CreateClassesAction, CreateClassesResult> {

    @Nonnull
    private final CreateClassesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Inject
    public CreateClassesActionHandler(@Nonnull AccessManager accessManager,

                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull CreateClassesChangeGeneratorFactory changeFactory,
                                      @Nonnull EntityNodeRenderer entityNodeRenderer) {
        super(accessManager, applyChanges);
        this.changeGeneratorFactory = checkNotNull(changeFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
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
        return new CreateClassesResult(action.changeRequestId(),
                                       action.projectId(),
                                       classes.stream().map(entityNodeRenderer::render).collect(toImmutableSet()));
    }
}
