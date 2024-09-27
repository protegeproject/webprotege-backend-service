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
import edu.stanford.protege.webprotege.criteria.HierarchyFilterType;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.criteria.SubClassOfCriteria;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import edu.stanford.protege.webprotege.portlet.PortletId;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GetEntityCardDescriptorsProjectActionHandler implements ProjectActionHandler<GetEntityCardDescriptorsRequest, GetEntityCardDescriptorsResponse> {

    private final AccessManager accessManager;

    private final ProjectId projectId;

    private final EntityMatcherFactory entityMatcherFactory;

    public GetEntityCardDescriptorsProjectActionHandler(AccessManager accessManager,
                                                        ProjectId projectId,
                                                        EntityMatcherFactory entityMatcherFactory) {
        this.accessManager = accessManager;
        this.projectId = projectId;
        this.entityMatcherFactory = entityMatcherFactory;
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
        var descriptors = getDummyDescriptors();
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
                // Filter descriptors based on read access requirements
                .filter(descriptor -> {
                    var requiredActions = descriptor.requiredReadActions();
                    // If no actions are specified then this is vacuously true.  Shortcut here that
                    // avoids a call to the access manager.
                    if(requiredActions.isEmpty()) {
                        return true;
                    }
                    var actionClosure = accessManager.getActionClosure(Subject.forUser(executionContext.userId()),
                            ProjectResource.forProject(projectId),
                            executionContext);
                    return actionClosure.containsAll(requiredActions);
                })
                .toList();
        return new GetEntityCardDescriptorsResponse(projectId, filteredDescriptors);
    }

    private static boolean isCriteriaVacuouslyTrue(CompositeRootCriteria visibilitiedCriteria) {
        return visibilitiedCriteria.equals(CompositeRootCriteria.get(List.of(), MultiMatchType.ALL));
    }


    private static @NotNull List<CardDescriptor> getDummyDescriptors() {

        List<CardDescriptor> cardDescriptors = new ArrayList<>();
        cardDescriptors.add(
                CardDescriptor.create(
                        CardId.valueOf("00000000-1111-1111-1111-111111111111"),
                        LanguageMap.of("en", "Commented entities"),
                        null, null,
                        PortletCardContentDescriptor.create(PortletId.valueOf("portlets.EntityEditor")),
                        new HashSet<>(),
                        new HashSet<>(),
                        CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL)
                ));

        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("00000000-1111-1111-1111-11111111111b"),
                LanguageMap.of("en", "Vizualization"),
                null, null,
                PortletCardContentDescriptor.create(PortletId.valueOf("portlets.viz")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL)
        ));

        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("00000000-1111-1111-1111-111111111112"),
                LanguageMap.of("en", "Basic details"),
                Color.get(255, 255, 255),
                Color.get(255, 0, 255),
                FormCardContentDescriptor.create(FormId.get("b4c85b41-e37c-442b-ac8b-7c6abe80f012")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ALL)
        ));

        cardDescriptors.add(CardDescriptor.create(
                CardId.valueOf("00000000-1111-1111-1111-111111111113"),
                LanguageMap.of("en", "Other details"),
                null, null,
                FormCardContentDescriptor.create(FormId.get("e7220610-68ce-4887-834e-1879e525ba23")),
                new HashSet<>(),
                new HashSet<>(),
                CompositeRootCriteria.get(ImmutableList.of(
                        SubClassOfCriteria.get(
                                new OWLClassImpl(IRI.create("http://www.example.org/RXPyJQtIXaZ1ikA84KDlcK")),
                                HierarchyFilterType.ALL
                        )
                ), MultiMatchType.ALL)
        ));
        return cardDescriptors;
    }
}
