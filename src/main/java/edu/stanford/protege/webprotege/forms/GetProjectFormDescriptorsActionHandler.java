package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class GetProjectFormDescriptorsActionHandler extends AbstractProjectActionHandler<GetProjectFormDescriptorsAction, GetProjectFormDescriptorsResult> {


    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Nonnull
    private final EntityFormSelectorRepository selectorRepository;

    @Inject
    public GetProjectFormDescriptorsActionHandler(@Nonnull AccessManager accessManager,
                                                  @Nonnull EntityFormRepository entityFormRepository,
                                                  @Nonnull EntityFormSelectorRepository selectorRepository) {
        super(accessManager);
        this.entityFormRepository = checkNotNull(entityFormRepository);
        this.selectorRepository = checkNotNull(selectorRepository);
    }

    @Nonnull
    @Override
    public Class<GetProjectFormDescriptorsAction> getActionClass() {
        return GetProjectFormDescriptorsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetProjectFormDescriptorsAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public GetProjectFormDescriptorsResult execute(@Nonnull GetProjectFormDescriptorsAction action,
                                                   @Nonnull ExecutionContext executionContext) {
        ProjectId projectId = action.projectId();
        var formDescriptors = entityFormRepository.findFormDescriptors(projectId).collect(toImmutableList());
        var selectors = selectorRepository.findFormSelectors(projectId)
                                          .collect(toImmutableList());

        return new GetProjectFormDescriptorsResult(projectId, formDescriptors, selectors);
    }
}
