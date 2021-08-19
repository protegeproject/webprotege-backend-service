package edu.stanford.protege.webprotege.api.resources;



import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.form.EntityFormDescriptor;
import edu.stanford.protege.webprotege.form.FormDescriptor;
import edu.stanford.protege.webprotege.form.FormId;
import edu.stanford.protege.webprotege.form.GetEntityFormDescriptorAction;
import edu.stanford.protege.webprotege.match.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.match.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-22
 */
public class FormResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormId formId;

    @Nonnull
    private final ActionExecutor actionExecutor;


    public FormResource(@Nonnull ProjectId projectId, @Nonnull FormId formId,
                        @Nonnull ActionExecutor actionExecutor) {
        this.projectId = projectId;
        this.formId = formId;
        this.actionExecutor = actionExecutor;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/")
    public Response getForm(@Context UserId userId, @Context ExecutionContext executionContext) {
        var actionResult = actionExecutor.execute(GetEntityFormDescriptorAction.create(projectId, formId),
                                                  new edu.stanford.protege.webprotege.ipc.ExecutionContext(executionContext.getUserId()));
        var formDescriptor = actionResult.getFormDescriptor().orElse(FormDescriptor.empty(formId));
        var criteria = actionResult.getFormSelectorCriteria().orElse(CompositeRootCriteria.get(ImmutableList.of(), MultiMatchType.ANY));
        var purpose = actionResult.getPurpose();
        var result = EntityFormDescriptor.valueOf(projectId, formId, formDescriptor, purpose, criteria);
        return Response.accepted(result).build();
    }


}
