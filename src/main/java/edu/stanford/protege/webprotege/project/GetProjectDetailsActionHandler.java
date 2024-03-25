package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.dispatch.ApplicationActionHandler;
import edu.stanford.protege.webprotege.dispatch.RequestContext;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
public class GetProjectDetailsActionHandler implements ApplicationActionHandler<GetProjectDetailsAction, GetProjectDetailsResult> {

    private final ProjectDetailsManager projectDetailsManager;

    @Inject
    public GetProjectDetailsActionHandler(ProjectDetailsManager projectDetailsManager) {
        this.projectDetailsManager = checkNotNull(projectDetailsManager);
    }

    @Nonnull
    @Override
    public Class<GetProjectDetailsAction> getActionClass() {
        return GetProjectDetailsAction.class;
    }

    @Nonnull
    @Override
    public RequestValidator getRequestValidator(@Nonnull GetProjectDetailsAction action, @Nonnull RequestContext requestContext) {
        return NullValidator.get();
    }

    @Nonnull
    @Override
    public GetProjectDetailsResult execute(@Nonnull GetProjectDetailsAction action, @Nonnull ExecutionContext executionContext) {
        ProjectDetails projectDetails = projectDetailsManager.getProjectDetails(action.projectId());
        return GetProjectDetailsResult.get(projectDetails);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectDetailsActionHandler")
                .toString();
    }
}
