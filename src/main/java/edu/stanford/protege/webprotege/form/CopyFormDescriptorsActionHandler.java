package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class CopyFormDescriptorsActionHandler extends AbstractProjectActionHandler<CopyFormDescriptorsAction, CopyFormDescriptorsResult> {

    @Nonnull
    private final FormsCopierFactory formsCopierFactory;

    @Inject
    public CopyFormDescriptorsActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull FormsCopierFactory formsCopierFactory) {
        super(accessManager);
        this.formsCopierFactory = checkNotNull(formsCopierFactory);
    }

    @Nonnull
    @Override
    public Class<CopyFormDescriptorsAction> getActionClass() {
        return CopyFormDescriptorsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CopyFormDescriptorsAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public CopyFormDescriptorsResult execute(@Nonnull CopyFormDescriptorsAction action,
                                             @Nonnull ExecutionContext executionContext) {
        var copyFromProjectId = action.getToProjectId();
        var copyToProjectId = action.getProjectId();
        var formsToCopy = action.getFormIds();
        var copier = formsCopierFactory.create(copyFromProjectId, copyToProjectId, formsToCopy);
        var copiedFormDescriptors = copier.copyForms();
        return CopyFormDescriptorsResult.create(copiedFormDescriptors);
    }
}
