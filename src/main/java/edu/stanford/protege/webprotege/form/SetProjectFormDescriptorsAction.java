package edu.stanford.protege.webprotege.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-23
 */
public class SetProjectFormDescriptorsAction implements ProjectAction<SetProjectFormDescriptorsResult> {

    private ProjectId projectId;

    private ImmutableList<FormDescriptor> formDescriptors;

    public SetProjectFormDescriptorsAction(ProjectId projectId,
                                           ImmutableList<FormDescriptor> formDescriptors) {
        this.projectId = checkNotNull(projectId);
        this.formDescriptors = checkNotNull(formDescriptors);
    }

    private SetProjectFormDescriptorsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public ImmutableList<FormDescriptor> getFormDescriptors() {
        return formDescriptors;
    }
}