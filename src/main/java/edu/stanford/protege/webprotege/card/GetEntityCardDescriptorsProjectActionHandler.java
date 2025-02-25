package edu.stanford.protege.webprotege.card;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.color.Color;
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
            cardDescriptorRepository.setCardDescriptors(action.projectId(), getDummyDescriptors());
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


    private static @NotNull List<CardDescriptor> getDummyDescriptors() {
        List<CardDescriptor> cardDescriptors = new ArrayList<>();
        cardDescriptors.add(
                CardDescriptor.create(
                        CardId.valueOf("00000000-1111-1111-1111-111111111111"),
                        LanguageMap.of("en", "Entity Iri"),
                        null,
                        null,
                        CustomContentEntityCardContentDescriptor.create(CustomContentId.valueOf("Hello.World")),
                        new HashSet<>(),
                        new HashSet<>(),
                        CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL)
                ));



        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("00000000-1111-1111-1111-111111111113"),
                LanguageMap.of("en", "Basic details"),
                null, null,
                FormCardContentDescriptor.create(FormId.get("4eb87d90-4c61-4862-b2c4-f8db52f374e9")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(
                ), MultiMatchType.ALL)
        ));
        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("8d8adabb-0270-487e-aafe-b9d646817f68"),
                LanguageMap.of("en", "Relationships"),
                null, null,
                FormCardContentDescriptor.create(FormId.get("b14a3efe-54c4-48e5-94e5-edb6088aefe5")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(
                ), MultiMatchType.ALL)
        ));
        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("46016c4b-baa6-418f-b6f6-1ce8f594c01b"),
                LanguageMap.of("en", "Test Missing Form"),
                null, null,
                FormCardContentDescriptor.create(FormId.get("ba1fd035-90b8-43b2-bc30-fc8c177c6489")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(
                ), MultiMatchType.ALL)
        ));
        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("760a907c-be4e-4654-915a-e145014b7580"),
                LanguageMap.of("en", "Usage"),
                null, null,
                CustomContentEntityCardContentDescriptor.create(CustomContentId.valueOf("class.stats")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(
                ), MultiMatchType.ALL)
        ));
        return cardDescriptors;
    }

    /*

    SubClassOfCriteria.get(
                                new OWLClassImpl(IRI.create("http://www.example.org/RXPyJQtIXaZ1ikA84KDlcK")),
                                HierarchyFilterType.ALL
                        )
     */
}
