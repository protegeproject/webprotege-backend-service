package edu.stanford.protege.webprotege.logicaldefinitions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.bulkop.ParentsChangedEvent;
import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasApplyChanges;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectChangeHandler;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.PackagedProjectChangeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Collections;

public class UpdateLogicalDefinitionsActionHandler extends AbstractProjectChangeHandler<Boolean, UpdateLogicalDefinitionsAction, UpdateLogicalDefinitionsResponse> {

    @Nonnull
    private final UpdateLogicalDefinitionsChangeListGeneratorFactory factory;

    @Nonnull
    private final EventDispatcher eventDispatcher;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    private final LogicalDefinitionExtractor logicalDefinitionExtractor;

    private final NecessaryConditionsExtractor necessaryConditionsExtractor;

    public UpdateLogicalDefinitionsActionHandler(
            @NotNull AccessManager accessManager,
            @NotNull HasApplyChanges applyChanges,
            @NotNull UpdateLogicalDefinitionsChangeListGeneratorFactory factory, @Nonnull EventDispatcher eventDispatcher, DefaultOntologyIdManager defaultOntologyIdManager, LogicalDefinitionExtractor logicalDefinitionExtractor, NecessaryConditionsExtractor necessaryConditionsExtractor) {
        super(accessManager, applyChanges);
        this.factory = factory;
        this.eventDispatcher = eventDispatcher;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.logicalDefinitionExtractor = logicalDefinitionExtractor;
        this.necessaryConditionsExtractor = necessaryConditionsExtractor;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(UpdateLogicalDefinitionsAction action, ExecutionContext executionContext) {
        return factory.create(action.changeRequestId(), action.projectId(), action.subject(),
                action.commitMessage(), action.pristineLogicalConditions(), action.changedLogicalConditions(), defaultOntologyIdManager, logicalDefinitionExtractor, necessaryConditionsExtractor);
    }

    @Override
    protected UpdateLogicalDefinitionsResponse createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, UpdateLogicalDefinitionsAction action, ExecutionContext executionContext) {
        if(changeApplicationResult.getSubject()) {
            EventId eventId = EventId.generate();
            eventDispatcher.dispatchEvent(new PackagedProjectChangeEvent(action.projectId(), eventId, Collections.singletonList(new ParentsChangedEvent(action.projectId(), eventId, action.subject().getIRI()))));
        }
        return new UpdateLogicalDefinitionsResponse();
    }

    @NotNull
    @Override
    public Class<UpdateLogicalDefinitionsAction> getActionClass() {
        return UpdateLogicalDefinitionsAction.class;
    }
}
