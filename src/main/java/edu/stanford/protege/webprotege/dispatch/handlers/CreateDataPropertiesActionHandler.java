package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.CreateDataPropertiesChangeGeneratorFactory;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.entity.CreateDataPropertiesAction;
import edu.stanford.protege.webprotege.entity.CreateDataPropertiesResult;
import edu.stanford.protege.webprotege.entity.EntityNodeRenderer;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.protege.webprotege.access.BuiltInAction.CREATE_PROPERTY;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 25/03/2013
 */
public class CreateDataPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLDataProperty>, CreateDataPropertiesAction, CreateDataPropertiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CreateDataPropertiesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Inject
    public CreateDataPropertiesActionHandler(@Nonnull AccessManager accessManager,

                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull CreateDataPropertiesChangeGeneratorFactory changeGeneratorFactory, @Nonnull EntityNodeRenderer entityNodeRenderer) {
        super(accessManager, applyChanges);
        this.projectId = checkNotNull(projectId);
        this.changeGeneratorFactory = checkNotNull(changeGeneratorFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
    }

    @Override
    protected ChangeListGenerator<Set<OWLDataProperty>> getChangeListGenerator(CreateDataPropertiesAction action,
                                                                               ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.sourceText(),
                                             action.langTag(),
                                             action.parents(),
                                             action.changeRequestId());
    }

    @Override
    protected CreateDataPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLDataProperty>> changeApplicationResult,
                                                            CreateDataPropertiesAction action,
                                                            ExecutionContext executionContext) {
        Map<OWLDataProperty, String> map = new HashMap<>();
        Set<OWLDataProperty> properties = changeApplicationResult.getSubject();
        return new CreateDataPropertiesResult(projectId,
                                              properties.stream()
                                                    .map(entityNodeRenderer::render)
                                                    .collect(toImmutableSet()));
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CreateDataPropertiesAction action) {
        return CREATE_PROPERTY;
    }

    @Nonnull
    @Override
    public Class<CreateDataPropertiesAction> getActionClass() {
        return CreateDataPropertiesAction.class;
    }
}
