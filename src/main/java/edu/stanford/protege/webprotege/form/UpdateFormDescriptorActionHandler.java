package edu.stanford.protege.webprotege.form;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class UpdateFormDescriptorActionHandler extends AbstractProjectActionHandler<UpdateFormDescriptorAction, UpdateFormDescriptorResult> {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    @Inject
    public UpdateFormDescriptorActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EntityFormRepository entityFormRepository) {
        super(accessManager);
        this.entityFormRepository = checkNotNull(entityFormRepository);
    }

    @Nonnull
    @Override
    public Class<UpdateFormDescriptorAction> getActionClass() {
        return UpdateFormDescriptorAction.class;
    }

    @Nonnull
    @Override
    public UpdateFormDescriptorResult execute(@Nonnull UpdateFormDescriptorAction action,
                                              @Nonnull ExecutionContext executionContext) {
        var projectId = action.getProjectId();
        var formDescriptor = action.getFormDescriptor();
        entityFormRepository.saveFormDescriptor(projectId, formDescriptor);
        return UpdateFormDescriptorResult.create();
    }
}
