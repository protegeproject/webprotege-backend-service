package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.protege.webprotege.access.BuiltInAction.EDIT_PROJECT_TAGS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public class SetProjectTagsActionHandler extends AbstractProjectActionHandler<SetProjectTagsAction, SetProjectTagsResult> {

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public SetProjectTagsActionHandler(@Nonnull AccessManager accessManager, @Nonnull TagsManager tagsManager) {
        super(accessManager);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Nonnull
    @Override
    public Class<SetProjectTagsAction> getActionClass() {
        return SetProjectTagsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetProjectTagsAction action) {
        return EDIT_PROJECT_TAGS;
    }

    @Nonnull
    @Override
    public SetProjectTagsResult execute(@Nonnull SetProjectTagsAction action, @Nonnull ExecutionContext executionContext) {
        tagsManager.setProjectTags(action.tagData());
        return new SetProjectTagsResult();
    }
}
