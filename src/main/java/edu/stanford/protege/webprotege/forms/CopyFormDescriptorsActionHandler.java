package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

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
        var copyFromProjectId = action.fromProject();
        var copyToProjectId = action.toProject();
        var formsToCopy = action.formIds();
        var copier = formsCopierFactory.create(copyFromProjectId, copyToProjectId, ImmutableList.copyOf(formsToCopy));
        var copiedFormDescriptors = copier.copyForms();
        return new CopyFormDescriptorsResult(copiedFormDescriptors);
    }
}
