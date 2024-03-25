package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
public class DeleteFormActionHandler extends AbstractProjectActionHandler<DeleteFormAction, DeleteFormResult> {


    @Nonnull
    private final EntityFormRepository entityFormRepository;


    @Inject
    public DeleteFormActionHandler(@Nonnull AccessManager accessManager,
                                   @Nonnull EntityFormRepository entityFormRepository) {
        super(accessManager);
        this.entityFormRepository = entityFormRepository;
    }

    @Nonnull
    @Override
    public Class<DeleteFormAction> getActionClass() {
        return DeleteFormAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(DeleteFormAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public DeleteFormResult execute(@Nonnull DeleteFormAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var formId = action.formId();
        entityFormRepository.deleteFormDescriptor(projectId, formId);
        return new DeleteFormResult();
    }
}
