package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.project.ProjectDetailsManager;

import javax.annotation.Nonnull;

public class GetIsExistingProjectActionHandler extends AbstractProjectActionHandler<GetIsExistingProjectAction, GetIsExistingProjectResult> {

    private final ProjectDetailsManager projectDetailsManager;

    public GetIsExistingProjectActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull ProjectDetailsManager projectDetailsManager) {
        super(accessManager);
        this.projectDetailsManager = projectDetailsManager;
    }

    @Nonnull
    @Override
    public Class<GetIsExistingProjectAction> getActionClass() {
        return GetIsExistingProjectAction.class;
    }

    @Nonnull
    @Override
    public GetIsExistingProjectResult execute(@Nonnull GetIsExistingProjectAction action, @Nonnull ExecutionContext executionContext) {
        var isExistingProject = projectDetailsManager.isExistingProject(action.projectId());
        return GetIsExistingProjectResult.create(isExistingProject);
    }
}
