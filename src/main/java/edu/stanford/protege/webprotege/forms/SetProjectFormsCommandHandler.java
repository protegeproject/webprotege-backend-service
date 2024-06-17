package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.ipc.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-06-15
 */
@WebProtegeHandler
public class SetProjectFormsCommandHandler implements AuthorizedCommandHandler<SetProjectFormsRequest, SetProjectFormsResult> {

    private final EntityFormRepository entityFormRepository;

    private final EntityFormSelectorRepository selectorRepository;

    public SetProjectFormsCommandHandler(EntityFormRepository entityFormRepository,
                                         EntityFormSelectorRepository selectorRepository) {
        this.entityFormRepository = entityFormRepository;
        this.selectorRepository = selectorRepository;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetProjectFormsRequest.CHANNEL;
    }

    @Override
    public Class<SetProjectFormsRequest> getRequestClass() {
        return SetProjectFormsRequest.class;
    }

    @Nonnull
    @Override
    public Resource getTargetResource(SetProjectFormsRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @Nonnull
    @Override
    public Collection<ActionId> getRequiredCapabilities() {
        return Collections.singleton(BuiltInAction.EDIT_FORMS.getActionId());
    }

    @Override
    public Mono<SetProjectFormsResult> handleRequest(SetProjectFormsRequest request,
                                                     ExecutionContext executionContext) {
        entityFormRepository.setProjectFormDescriptors(request.projectId(),
                                                       request.formDescriptors());

        // Make sure that the project id matches up to the id in the request
        request.formSelectors()
                .stream()
                .map(selector -> EntityFormSelector.get(request.projectId(),
                                                        selector.getCriteria(),
                                                        selector.getPurpose(),
                                                        selector.getFormId()))
                .forEach(selectorRepository::save);
        return Mono.just(new SetProjectFormsResult());
    }
}
