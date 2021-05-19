package edu.stanford.protege.webprotege.form;


import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.match.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class SetEntityFormDescriptorAction implements ProjectAction<SetEntityFormDescriptorResult> {

    private ProjectId projectId;

    private FormDescriptor formDescriptor;

    private CompositeRootCriteria selectorCriteria;

    private FormPurpose purpose;


    public SetEntityFormDescriptorAction(@Nonnull ProjectId projectId,
                                         @Nonnull FormDescriptor formDescriptor,
                                         @Nonnull FormPurpose purpose,
                                         @Nullable CompositeRootCriteria selectorCriteria) {
        this.projectId = checkNotNull(projectId);
        this.formDescriptor = checkNotNull(formDescriptor);
        this.purpose = checkNotNull(purpose);
        this.selectorCriteria = checkNotNull(selectorCriteria);
    }


    private SetEntityFormDescriptorAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public FormDescriptor getFormDescriptor() {
        return formDescriptor;
    }

    @Nonnull
    public Optional<CompositeRootCriteria> getSelectorCriteria() {
        return Optional.ofNullable(selectorCriteria);
    }

    public FormPurpose getPurpose() {
        return purpose;
    }
}
