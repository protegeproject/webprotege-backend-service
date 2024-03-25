package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.dispatch.actions.AddAxiomsAction;
import edu.stanford.protege.webprotege.dispatch.actions.AddAxiomsResult;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class AddAxiomsActionHandler extends AbstractProjectActionHandler<AddAxiomsAction, AddAxiomsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final AddAxiomsChangeListGeneratorFactory changeListGeneratorFactory;

    @Inject
    public AddAxiomsActionHandler(@Nonnull AccessManager accessManager,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull ChangeManager changeManager,
                                  @Nonnull AddAxiomsChangeListGeneratorFactory changeListGeneratorFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.changeManager = changeManager;
        this.changeListGeneratorFactory = changeListGeneratorFactory;
    }

    @Nonnull
    @Override
    public Class<AddAxiomsAction> getActionClass() {
        return AddAxiomsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(AddAxiomsAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public AddAxiomsResult execute(@Nonnull AddAxiomsAction action, @Nonnull ExecutionContext executionContext) {
        var changeListGenerator = changeListGeneratorFactory.create(action);
        var result = changeManager.applyChanges(executionContext.userId(), changeListGenerator);
        int addedAxiomsCount = result.getChangeList()
                                     .size();
        return new AddAxiomsResult(projectId, addedAxiomsCount);
    }
}
