package edu.stanford.bmir.protege.web.server.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import edu.stanford.bmir.protege.web.server.app.GetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.server.app.SetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.server.auth.ChangePasswordAction;
import edu.stanford.bmir.protege.web.server.auth.PerformLoginAction;
import edu.stanford.bmir.protege.web.server.bulkop.EditAnnotationsAction;
import edu.stanford.bmir.protege.web.server.bulkop.MoveEntitiesToParentAction;
import edu.stanford.bmir.protege.web.server.bulkop.SetAnnotationValueAction;
import edu.stanford.bmir.protege.web.server.change.GetProjectChangesAction;
import edu.stanford.bmir.protege.web.server.change.GetWatchedEntityChangesAction;
import edu.stanford.bmir.protege.web.server.change.RevertRevisionAction;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordAction;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.server.entity.*;
import edu.stanford.bmir.protege.web.server.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.server.form.*;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsAction;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsPageContainingIndividualAction;
import edu.stanford.bmir.protege.web.server.issues.*;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdCompletionsAction;
import edu.stanford.bmir.protege.web.server.itemlist.GetPossibleItemCompletionsAction;
import edu.stanford.bmir.protege.web.server.itemlist.GetUserIdCompletionsAction;
import edu.stanford.bmir.protege.web.server.lang.GetProjectLangTagsAction;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.server.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeAction;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.server.merge_add.ExistingOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.server.merge_add.GetAllOntologiesAction;
import edu.stanford.bmir.protege.web.server.merge_add.NewOntologyMergeAddAction;
import edu.stanford.bmir.protege.web.server.obo.*;
import edu.stanford.bmir.protege.web.server.ontology.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.server.ontology.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.server.ontology.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.server.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.server.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.server.perspective.*;
import edu.stanford.bmir.protege.web.server.project.*;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityHtmlRenderingAction;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.server.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.server.search.GetSearchSettingsAction;
import edu.stanford.bmir.protege.web.server.search.PerformEntitySearchAction;
import edu.stanford.bmir.protege.web.server.search.SetSearchSettingsAction;
import edu.stanford.bmir.protege.web.server.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.server.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.server.tag.*;
import edu.stanford.bmir.protege.web.server.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.server.user.CreateUserAccountAction;
import edu.stanford.bmir.protege.web.server.user.GetCurrentUserInSessionAction;
import edu.stanford.bmir.protege.web.server.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.server.viz.GetEntityGraphAction;
import edu.stanford.bmir.protege.web.server.viz.GetUserProjectEntityGraphCriteriaAction;
import edu.stanford.bmir.protege.web.server.viz.SetEntityGraphActiveFiltersAction;
import edu.stanford.bmir.protege.web.server.viz.SetUserProjectEntityGraphSettingsAction;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesAction;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     The basic interface for actions that are sent to the dispatch service
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action")
@JsonSubTypes(value = {
        @Type(value = AddEntityCommentAction.class),
        @Type(value = AddProjectTagAction.class),
        @Type(value = BatchAction.class),
        @Type(value = ChangePasswordAction.class),
        @Type(value = CheckManchesterSyntaxFrameAction.class),
        @Type(value = ComputeProjectMergeAction.class),
        @Type(value = CopyFormDescriptorsFromProjectAction.class),
        @Type(value = CreateAnnotationPropertiesAction.class),
        @Type(value = CreateClassesAction.class),
        @Type(value = CreateDataPropertiesAction.class),
        @Type(value = CreateNamedIndividualsAction.class),
        @Type(value = CreateObjectPropertiesAction.class),
        @Type(value = CreateEntityDiscussionThreadAction.class),
        @Type(value = CreateEntityFromFormDataAction.class),
        @Type(value = GetAnnotationPropertyFrameAction.class),
        @Type(value = CreateNewProjectAction.class),
        @Type(value = LoadProjectAction.class),
        @Type(value = LogOutUserAction.class),
        @Type(value = RebuildPermissionsAction.class),
        @Type(value = CreateUserAccountAction.class),
        @Type(value = DeleteEntitiesAction.class),
        @Type(value = DeleteEntityCommentAction.class),
        @Type(value = DeleteFormAction.class),
        @Type(value = EditAnnotationsAction.class),
        @Type(value = EditCommentAction.class),
        @Type(value = ExistingOntologyMergeAddAction.class),
        @Type(value = GetAllOntologiesAction.class),
        @Type(value = GetApplicationSettingsAction.class),
        @Type(value = GetAvailableProjectsAction.class),
        @Type(value = GetClassFrameAction.class),
        @Type(value = GetDataPropertyFrameAction.class),
        @Type(value = GetCommentedEntitiesAction.class),
        @Type(value = GetAvailableProjectsWithPermissionAction.class),
        @Type(value = GetCurrentUserInSessionAction.class),
        @Type(value = GetDeprecatedEntitiesAction.class),
        @Type(value = GetEmailAddressAction.class),
        @Type(value = GetEntityCreationFormsAction.class),
        @Type(value = GetEntityCrudKitsAction.class),
        @Type(value = GetEntityDeprecationFormsAction.class),
        @Type(value = GetEntityDiscussionThreadsAction.class),
        @Type(value = GetEntityFormDescriptorAction.class),
        @Type(value = GetEntityFormsAction.class),
        @Type(value = GetEntityGraphAction.class),
        @Type(value = GetEntityRenderingAction.class),
        @Type(value = GetEntityTagsAction.class),
        @Type(value = GetHeadRevisionNumberAction.class),
        @Type(value = GetHierarchyChildrenAction.class),
        @Type(value = GetHierarchyPathsToRootAction.class),
        @Type(value = GetHierarchyRootsAction.class),
        @Type(value = GetHierarchySiblingsAction.class),
        @Type(value = GetEntityHtmlRenderingAction.class),
        @Type(value = GetIndividualsAction.class),
        @Type(value = GetIndividualsPageContainingIndividualAction.class),
        @Type(value = GetManchesterSyntaxFrameAction.class),
        @Type(value = GetManchesterSyntaxFrameCompletionsAction.class),
        @Type(value = GetMatchingEntitiesAction.class),
        @Type(value = GetNamedIndividualFrameAction.class),
        @Type(value = GetObjectPropertyFrameAction.class),
        @Type(value = GetOboNamespacesAction.class),
        @Type(value = GetOboTermIdAction.class),
        @Type(value = GetOboTermCrossProductAction.class),
        @Type(value = GetOboTermDefinitionAction.class),
        @Type(value = GetOboTermSynonymsAction.class),
        @Type(value = GetOboTermRelationshipsAction.class),
        @Type(value = GetOboTermXRefsAction.class),
        @Type(value = GetOntologyAnnotationsAction.class),
        @Type(value = GetOntologyFramesAction.class),
        @Type(value = GetPersonIdCompletionsAction.class),
        @Type(value = GetPerspectiveDetailsAction.class),
        @Type(value = GetPerspectiveLayoutAction.class),
        @Type(value = GetPerspectivesAction.class),
        @Type(value = GetPossibleItemCompletionsAction.class),
        @Type(value = GetProjectChangesAction.class),
        @Type(value = GetProjectDetailsAction.class),
        @Type(value = GetProjectEventsAction.class),
        @Type(value = GetProjectFormDescriptorsAction.class),
        @Type(value = GetProjectInfoAction.class),
        @Type(value = GetPerspectiveLayoutAction.class),
        @Type(value = GetProjectPermissionsAction.class),
        @Type(value = GetProjectPrefixDeclarationsAction.class),
        @Type(value = GetProjectSettingsAction.class),
        @Type(value = GetProjectSharingSettingsAction.class),
        @Type(value = GetProjectTagsAction.class),
        @Type(value = GetProjectLangTagsAction.class),
        @Type(value = GetRevisionSummariesAction.class),
        @Type(value = GetRootOntologyIdAction.class),
        @Type(value = GetSearchSettingsAction.class),
        @Type(value = GetUserIdCompletionsAction.class),
        @Type(value = GetUserProjectEntityGraphCriteriaAction.class),
        @Type(value = GetUsageAction.class),
        @Type(value = GetWatchesAction.class),
        @Type(value = GetWatchedEntityChangesAction.class),
        @Type(value = LoadProjectAction.class),
        @Type(value = LogOutUserAction.class),
        @Type(value = LookupEntitiesAction.class),
        @Type(value = MergeEntitiesAction.class),
        @Type(value = MergeUploadedProjectAction.class),
        @Type(value = MoveEntitiesToParentAction.class),
        @Type(value = MoveHierarchyNodeAction.class),
        @Type(value = MoveProjectsToTrashAction.class),
        @Type(value = NewOntologyMergeAddAction.class),
        @Type(value = PerformEntitySearchAction.class),
        @Type(value = PerformLoginAction.class),
        @Type(value = RebuildPermissionsAction.class),
        @Type(value = RemoveProjectFromTrashAction.class),
        @Type(value = ResetPasswordAction.class),
        @Type(value = ResetPerspectivesAction.class),
        @Type(value = ResetPerspectiveLayoutAction.class),
        @Type(value = SetPerspectivesAction.class),
        @Type(value = ResetPerspectivesAction.class),
        @Type(value = RevertRevisionAction.class),
        @Type(value = SetAnnotationValueAction.class),
        @Type(value = SetApplicationSettingsAction.class),
        @Type(value = SetDiscussionThreadStatusAction.class),
        @Type(value = SetEmailAddressAction.class),
        @Type(value = SetEntityCrudKitSettingsAction.class),
        @Type(value = SetEntityFormDescriptorAction.class),
        @Type(value = SetEntityFormsDataAction.class),
        @Type(value = SetEntityGraphActiveFiltersAction.class),
        @Type(value = SetEntityWatchesAction.class),
        @Type(value = SetManchesterSyntaxFrameAction.class),
        @Type(value = SetOboTermCrossProductAction.class),
        @Type(value = SetOboTermDefinitionAction.class),
        @Type(value = SetOboTermIdAction.class),
        @Type(value = SetOboTermRelationshipsAction.class),
        @Type(value = SetOboTermSynonymsAction.class),
        @Type(value = SetOboTermXRefsAction.class),
        @Type(value = SetOntologyAnnotationsAction.class),
        @Type(value = SetPerspectiveLayoutAction.class),
        @Type(value = SetProjectFormDescriptorsAction.class),
        @Type(value = SetProjectPrefixDeclarationsAction.class),
        @Type(value = SetProjectSettingsAction.class),
        @Type(value = SetProjectSharingSettingsAction.class),
        @Type(value = SetProjectTagsAction.class),
        @Type(value = SetSearchSettingsAction.class),
        @Type(value = SetUserProjectEntityGraphSettingsAction.class),
        @Type(value = UpdateAnnotationPropertyFrameAction.class),
        @Type(value = UpdateClassFrameAction.class),
        @Type(value = UpdateDataPropertyFrameAction.class),
        @Type(value = UpdateEntityTagsAction.class),
        @Type(value = UpdateFormDescriptorAction.class),
        @Type(value = UpdateFrameAction.class),
        @Type(value = UpdateNamedIndividualFrameAction.class),
        @Type(value = UpdateObjectPropertyFrameAction.class)
})
public interface Action<R extends Result> {
}
