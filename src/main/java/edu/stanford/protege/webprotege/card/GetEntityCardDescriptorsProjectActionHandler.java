package edu.stanford.protege.webprotege.card;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.common.LanguageMap;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GetEntityCardDescriptorsProjectActionHandler implements ProjectActionHandler<GetEntityCardDescriptorsRequest, GetEntityCardDescriptorsResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetEntityCardDescriptorsProjectActionHandler.class);

    private final AccessManager accessManager;

    private final ProjectId projectId;

    private final EntityMatcherFactory entityMatcherFactory;

    private final EntityFormManager entityFormManager;

    private final CardDescriptorRepository cardDescriptorRepository;

    public GetEntityCardDescriptorsProjectActionHandler(AccessManager accessManager,
                                                        ProjectId projectId,
                                                        EntityMatcherFactory entityMatcherFactory, EntityFormManager entityFormManager, CardDescriptorRepository cardDescriptorRepository) {
        this.accessManager = accessManager;
        this.projectId = projectId;
        this.entityMatcherFactory = entityMatcherFactory;
        this.entityFormManager = entityFormManager;
        this.cardDescriptorRepository = cardDescriptorRepository;
    }

    @NotNull
    @Override
    public Class<GetEntityCardDescriptorsRequest> getActionClass() {
        return GetEntityCardDescriptorsRequest.class;
    }

    @NotNull
    @Override
    public RequestValidator getRequestValidator(@NotNull GetEntityCardDescriptorsRequest action, @NotNull RequestContext requestContext) {
        return new ProjectPermissionValidator(accessManager, projectId, requestContext.getUserId(), BuiltInAction.VIEW_PROJECT.getActionId());
    }

    @NotNull
    @Override
    public GetEntityCardDescriptorsResponse execute(@NotNull GetEntityCardDescriptorsRequest action, @NotNull ExecutionContext executionContext) {
        var descriptors = cardDescriptorRepository.getCardDescriptors(action.projectId());
        if(descriptors.isEmpty()) {
            cardDescriptorRepository.setCardDescriptors(action.projectId(), getExampleCard());
        }
        var formsForEntity = entityFormManager.getFormDescriptors(action.subject(), projectId, FormPurpose.ENTITY_EDITING)
                .stream()
                .map(FormDescriptor::getFormId)
                .collect(Collectors.toSet());
        // Descriptors for the project
        var filteredDescriptors = descriptors.stream()
                // Filter descriptors based on criteria
                .filter(descriptor -> {
                    // Vacuously true
                    var visibilityCriteria = descriptor.visibilityCriteria();
                    if(isCriteriaVacuouslyTrue(visibilityCriteria)) {
                        return true;
                    }
                    var matcher = entityMatcherFactory.getEntityMatcher(visibilityCriteria);
                    return matcher.matches(action.subject());
                })
                // We only display the form if it's actually appropriate
                .filter(descriptor -> {
                    if(descriptor.contentDescriptor() instanceof  FormCardContentDescriptor formContentDescriptor) {
                        return formsForEntity.contains(formContentDescriptor.formId());
                    }
                    else {
                        return true;
                    }
                }).toList();

                var actionClosure = accessManager.getActionClosure(Subject.forUser(executionContext.userId()),
                        ProjectResource.forProject(projectId),
                        executionContext);
                // Filter descriptors based on read access requirements
                var screenedDescriptors = filteredDescriptors.stream().filter(descriptor -> {
                    var requiredActions = descriptor.requiredReadActions();
                    // If no actions are specified then this is vacuously true.  Shortcut here that
                    // avoids a call to the access manager.
                    if(requiredActions.isEmpty()) {
                        return true;
                    }
                    return actionClosure.containsAll(requiredActions);
                })
                .toList();
                var writableCards = screenedDescriptors.stream()
                        .filter(descriptor -> actionClosure.containsAll(descriptor.requiredWriteActions()))
                        .map(CardDescriptor::cardId)
                        .toList();
        return new GetEntityCardDescriptorsResponse(projectId, screenedDescriptors, writableCards);
    }

    private static boolean isCriteriaVacuouslyTrue(CompositeRootCriteria visibilitiedCriteria) {
        return visibilitiedCriteria.equals(CompositeRootCriteria.get(List.of(), MultiMatchType.ALL));
    }


    private static @NotNull List<CardDescriptor> getExampleCard() {
        List<CardDescriptor> cardDescriptors = new ArrayList<>();
        cardDescriptors.add(
                CardDescriptor.create(
                        CardId.valueOf("00000000-1111-1111-1111-111111111111"),
                        LanguageMap.of("en", "Entity Iri"),
                        null,
                        null,
                        CustomContentEntityCardContentDescriptor.create(CustomContentId.valueOf("example.card")),
                        new HashSet<>(),
                        new HashSet<>(),
                        CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL)
                ));
        return cardDescriptors;
    }
}
