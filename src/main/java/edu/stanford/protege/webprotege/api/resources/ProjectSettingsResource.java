package edu.stanford.protege.webprotege.api.resources;



import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.crud.GetEntityCrudKitSettingsAction;
import edu.stanford.protege.webprotege.crud.IRIPrefixUpdateStrategy;
import edu.stanford.protege.webprotege.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.project.GetProjectPrefixDeclarationsAction;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.project.SetProjectPrefixDeclarationsAction;
import edu.stanford.protege.webprotege.projectsettings.AllProjectSettings;
import edu.stanford.protege.webprotege.projectsettings.GetProjectSettingsAction;
import edu.stanford.protege.webprotege.projectsettings.SetProjectSettingsAction;
import edu.stanford.protege.webprotege.search.GetSearchSettingsAction;
import edu.stanford.protege.webprotege.search.ProjectSearchSettings;
import edu.stanford.protege.webprotege.search.SetSearchSettingsAction;
import edu.stanford.protege.webprotege.sharing.GetProjectSharingSettingsAction;
import edu.stanford.protege.webprotege.sharing.SetProjectSharingSettingsAction;
import edu.stanford.protege.webprotege.tag.GetProjectTagsAction;
import edu.stanford.protege.webprotege.tag.SetProjectTagsAction;
import edu.stanford.protege.webprotege.tag.TagData;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsResource {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ActionExecutor actionExecutor;


    public ProjectSettingsResource(@Nonnull ProjectId projectId, @Nonnull ActionExecutor actionExecutor) {
        this.projectId = checkNotNull(projectId);
        this.actionExecutor = checkNotNull(actionExecutor);
    }

    @Path("/")
    @GET
    @Produces(APPLICATION_JSON)
    public Response getProjectSettings(@Context UserId userId, @Context ExecutionContext executionContext) {
        var projectSettingsResult = actionExecutor.execute(GetProjectSettingsAction.create(projectId), executionContext);
        var entityCreationSettingsResult = actionExecutor.execute(new GetEntityCrudKitSettingsAction(projectId),
                                                                  executionContext);
        var prefixDeclarationSettingsResult = actionExecutor.execute(GetProjectPrefixDeclarationsAction.create(projectId),
                                                                     executionContext);

        var tagsResult = actionExecutor.execute(GetProjectTagsAction.create(projectId), executionContext);
        var sharingSettings = actionExecutor.execute(GetProjectSharingSettingsAction.create(projectId), executionContext);
        var searchSettingsResult = actionExecutor.execute(GetSearchSettingsAction.create(projectId), executionContext);
        var projectSettings = AllProjectSettings.get(projectSettingsResult.getSettings(),
                                                     entityCreationSettingsResult.getSettings(),
                                                     ImmutableList.copyOf(prefixDeclarationSettingsResult.getPrefixDeclarations()),
                                                     ImmutableList.copyOf(tagsResult.getTags()),
                                                     sharingSettings.getProjectSharingSettings(),
                                                     ProjectSearchSettings.get(projectId, searchSettingsResult.getFilters()));
        var nilledOutProjectSettings = projectSettings.withProjectId(ProjectId.getNil());
        return Response.ok(nilledOutProjectSettings).build();
    }

    @Path("/")
    @POST
    @Consumes(APPLICATION_JSON)
    public Response setProjectSettings(@Context UserId userId, @Context ExecutionContext executionContext, AllProjectSettings allProjectSettings) {
        var projectSettings = allProjectSettings.getProjectSettings().withProjectId(projectId);
        actionExecutor.execute(SetProjectSettingsAction.create(projectSettings), executionContext);

        var entityCreationSettings = allProjectSettings.getEntityCreationSettings();
        var currentSettingsResult = actionExecutor.execute(new GetEntityCrudKitSettingsAction(projectId), executionContext);
        actionExecutor.execute(new SetEntityCrudKitSettingsAction(projectId,
                                                                  currentSettingsResult.getSettings(),
                                                                  entityCreationSettings,
                                                                  IRIPrefixUpdateStrategy.LEAVE_INTACT), executionContext);
        var prefixDeclarations = allProjectSettings.getPrefixDeclarations();
        actionExecutor.execute(SetProjectPrefixDeclarationsAction.create(projectId, prefixDeclarations), executionContext);

        var projectTags = allProjectSettings.getProjectTags();
        var projectTagsData = projectTags.stream()
                                         .map(tag -> tag.withProjectId(projectId))
                                         .map(tag -> TagData.get(tag.getTagId(),
                                                                 tag.getLabel(),
                                                                 tag.getDescription(),
                                                                 tag.getColor(),
                                                                 tag.getBackgroundColor(),
                                                                 tag.getCriteria(),
                                                                 0))
                                         .collect(toImmutableList());
        actionExecutor.execute(SetProjectTagsAction.create(projectId, projectTagsData), executionContext);
        var sharingSettings = allProjectSettings.getSharingSettings();
        actionExecutor.execute(SetProjectSharingSettingsAction.create(sharingSettings), executionContext);
        var searchSettings = allProjectSettings.getSearchSettings();
        actionExecutor.execute(SetSearchSettingsAction.create(projectId, ImmutableList.of(), searchSettings.getSearchFilters()), executionContext);
        return Response.ok().build();
    }
}
