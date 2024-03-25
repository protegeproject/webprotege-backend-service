package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.CreateObjectPropertiesChangeGeneratorFactory;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.entity.CreateObjectPropertiesAction;
import edu.stanford.protege.webprotege.entity.CreateObjectPropertiesResult;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_PROPERTY;
import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLObjectProperty>, CreateObjectPropertiesAction, CreateObjectPropertiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CreateObjectPropertiesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Inject
    public CreateObjectPropertiesActionHandler(@Nonnull AccessManager accessManager,

                                               @Nonnull HasApplyChanges applyChanges,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull CreateObjectPropertiesChangeGeneratorFactory changeGeneratorFactory,
                                               @Nonnull EntityNodeRenderer entityNodeRenderer) {
        super(accessManager, applyChanges);
        this.projectId = checkNotNull(projectId);
        this.changeGeneratorFactory = checkNotNull(changeGeneratorFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
    }

    @Nonnull
    @Override
    public Class<CreateObjectPropertiesAction> getActionClass() {
        return CreateObjectPropertiesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(CreateObjectPropertiesAction action) {
        return Arrays.asList(CREATE_PROPERTY, EDIT_ONTOLOGY);
    }

    @Override
    protected ChangeListGenerator<Set<OWLObjectProperty>> getChangeListGenerator(CreateObjectPropertiesAction action,
                                                                                 ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.sourceText(),
                                             action.langTag(),
                                             action.parents(),
                                             action.changeRequestId());
    }

    @Override
    protected CreateObjectPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLObjectProperty>> changeApplicationResult,
                                                              CreateObjectPropertiesAction action,
                                                              ExecutionContext executionContext) {
        Set<OWLObjectProperty> result = changeApplicationResult.getSubject();
        return new CreateObjectPropertiesResult(projectId,
                                                result.stream()
                                                      .map(entityNodeRenderer::render)
                                                      .collect(toImmutableSet()));
    }
}
