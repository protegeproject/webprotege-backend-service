package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Subject;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GetHierarchyDescriptorActionHandler extends AbstractProjectActionHandler<GetHierarchyDescriptorRequest, GetHierarchyDescriptorResponse> {

    private final AccessManager accessManager;

    private static final Logger logger = LoggerFactory.getLogger(GetHierarchyDescriptorActionHandler.class);

    private final HierarchyDescriptorRuleSelector ruleSelector;

    public GetHierarchyDescriptorActionHandler(@Nonnull AccessManager accessManager, HierarchyDescriptorRuleSelector ruleSelector) {
        super(accessManager);
        this.accessManager = accessManager;
        this.ruleSelector = ruleSelector;
    }

    @Nonnull
    @Override
    public Class<GetHierarchyDescriptorRequest> getActionClass() {
        return GetHierarchyDescriptorRequest.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(GetHierarchyDescriptorRequest action) {
        return BuiltInCapability.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetHierarchyDescriptorResponse execute(@Nonnull GetHierarchyDescriptorRequest action, @Nonnull ExecutionContext executionContext) {
        var displayContext = action.displayContext();
        logger.debug("GetHierarchyDescriptorRequest: {}", action);
        var userId = executionContext.userId();
        var actionClosure = accessManager.getCapabilityClosure(Subject.forUser(userId), ProjectResource.forProject(action.projectId()), executionContext);
        logger.debug("Action closure: {}", actionClosure);
        var selectedRule = ruleSelector.selectRule(action.projectId(), displayContext, actionClosure);
        logger.debug("Selected rule: {}", selectedRule);
        var hierarchyDescriptor = selectedRule.map(HierarchyDescriptorRule::hierarchyDescriptor).orElse(null);
        return new GetHierarchyDescriptorResponse(hierarchyDescriptor);
    }
}
