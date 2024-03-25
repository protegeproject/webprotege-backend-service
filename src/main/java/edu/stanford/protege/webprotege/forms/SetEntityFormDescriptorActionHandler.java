package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class SetEntityFormDescriptorActionHandler extends AbstractProjectActionHandler<SetEntityFormDescriptorAction, SetEntityFormDescriptorResult> {

    @Nonnull
    private final EntityFormRepository entityFormRepository;

    private final EntityFormSelectorRepository entityFormSelectorRepository;

    @Inject
    public SetEntityFormDescriptorActionHandler(@Nonnull AccessManager accessManager,
                                                @Nonnull EntityFormRepository entityFormRepository,
                                                EntityFormSelectorRepository entityFormSelectorRepository) {
        super(accessManager);
        this.entityFormRepository = entityFormRepository;
        this.entityFormSelectorRepository = entityFormSelectorRepository;
    }

    @Nonnull
    @Override
    public Class<SetEntityFormDescriptorAction> getActionClass() {
        return SetEntityFormDescriptorAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityFormDescriptorAction action) {
        return BuiltInAction.EDIT_FORMS;
    }

    @Nonnull
    @Override
    public SetEntityFormDescriptorResult execute(@Nonnull SetEntityFormDescriptorAction action,
                                                 @Nonnull ExecutionContext executionContext) {
        var formDescriptor = action.formDescriptor();
        var selectorCriteria = action.selectorCriteria();
        var projectId = action.projectId();
        entityFormRepository.saveFormDescriptor(projectId,
                                                formDescriptor);

        Optional.ofNullable(selectorCriteria).ifPresent(compositeRootCriteria -> {
            var formId = formDescriptor.getFormId();
            entityFormSelectorRepository.save(EntityFormSelector.get(
                    projectId,
                    compositeRootCriteria,
                    action.purpose(),
                    formId));
        });
        return new SetEntityFormDescriptorResult();
    }
}
