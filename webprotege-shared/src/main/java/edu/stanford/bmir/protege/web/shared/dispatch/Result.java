package edu.stanford.bmir.protege.web.shared.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsAction;
import edu.stanford.bmir.protege.web.shared.app.SetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.shared.auth.*;
import edu.stanford.bmir.protege.web.shared.bulkop.*;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsAction;
import edu.stanford.bmir.protege.web.shared.crud.GetEntityCrudKitsResult;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsAction;
import edu.stanford.bmir.protege.web.shared.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.*;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsAction;
import edu.stanford.bmir.protege.web.shared.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.issues.*;
import edu.stanford.bmir.protege.web.shared.itemlist.*;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressAction;
import edu.stanford.bmir.protege.web.shared.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectAction;
import edu.stanford.bmir.protege.web.shared.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.shared.merge_add.*;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsAction;
import edu.stanford.bmir.protege.web.shared.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.shared.perspective.*;
import edu.stanford.bmir.protege.web.shared.project.*;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsAction;
import edu.stanford.bmir.protege.web.shared.projectsettings.SetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;
import edu.stanford.bmir.protege.web.shared.search.*;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.GetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsAction;
import edu.stanford.bmir.protege.web.shared.sharing.SetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.shared.tag.*;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageAction;
import edu.stanford.bmir.protege.web.shared.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.shared.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserAction;
import edu.stanford.bmir.protege.web.shared.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.shared.viz.*;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesAction;
import edu.stanford.bmir.protege.web.shared.watches.SetEntityWatchesResult;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     The basic interface for results which are returned from the dispatch service
 * </p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action")
@JsonSubTypes({
        @Type(AddEntityCommentResult.class),
        @Type(AddProjectTagResult.class),
        @Type(BatchResult.class),
        @Type(ChangePasswordResult.class),
        @Type(CheckManchesterSyntaxFrameResult.class),
        @Type(ComputeProjectMergeResult.class),
        @Type(CopyFormDescriptorsFromProjectResult.class),
        @Type(CreateAnnotationPropertiesResult.class),
        @Type(CreateClassesResult.class),
        @Type(CreateDataPropertiesResult.class),
        @Type(CreateNamedIndividualsResult.class),
        @Type(CreateObjectPropertiesResult.class),
        @Type(CreateEntityDiscussionThreadResult.class),
        @Type(CreateEntityFromFormDataResult.class),
        @Type(CreateNewProjectResult.class),
        @Type(LoadProjectResult.class),
        @Type(LogOutUserResult.class),
        @Type(RebuildPermissionsResult.class),
        @Type(CreateUserAccountResult.class),
        @Type(DeleteEntitiesResult.class),
        @Type(DeleteEntityCommentResult.class),
        @Type(DeleteFormResult.class),
        @Type(EditAnnotationsResult.class),
        @Type(EditCommentResult.class),
        @Type(ExistingOntologyMergeAddResult.class),
        @Type(GetAllOntologiesResult.class),
        @Type(GetApplicationSettingsResult.class),
        @Type(GetCommentedEntitiesResult.class),
        @Type(GetAvailableProjectsResult.class),
        @Type(GetAvailableProjectsWithPermissionResult.class),
        @Type(GetCurrentUserInSessionResult.class),
        @Type(GetDeprecatedEntitiesResult.class),
        @Type(GetEmailAddressResult.class),
        @Type(GetEntityCreationFormsResult.class),
        @Type(GetEntityCrudKitsResult.class),
        @Type(GetEntityDeprecationFormsResult.class),
        @Type(GetEntityDiscussionThreadsResult.class),
        @Type(GetEntityFormDescriptorResult.class),
        @Type(GetEntityFormsResult.class),
        @Type(GetEntityGraphResult.class),
        @Type(GetEntityRenderingResult.class),
        @Type(GetEntityTagsResult.class),
        @Type(GetHeadRevisionNumberResult.class),
        @Type(GetHierarchyChildrenResult.class),
        @Type(GetHierarchyPathsToRootResult.class),
        @Type(GetHierarchyRootsResult.class),
        @Type(GetHierarchySiblingsResult.class),
        @Type(GetEntityHtmlRenderingResult.class),
        @Type(GetIndividualsResult.class),
        @Type(GetIndividualsPageContainingIndividualResult.class),
        @Type(GetManchesterSyntaxFrameResult.class),
        @Type(GetManchesterSyntaxFrameCompletionsResult.class),
        @Type(GetMatchingEntitiesResult.class),
        @Type(GetNamedIndividualFrameResult.class),
        @Type(GetObjectPropertyFrameResult.class),
        @Type(GetOboTermIdResult.class),
        @Type(GetOboNamespacesResult.class),
        @Type(GetOboTermCrossProductResult.class),
        @Type(GetOboTermDefinitionResult.class),
        @Type(GetOboTermSynonymsResult.class),
        @Type(GetOboTermRelationshipsResult.class),
        @Type(GetOboTermXRefsResult.class),
        @Type(GetOntologyAnnotationsResult.class),
        @Type(GetOntologyFramesResult.class),
        @Type(GetPersonIdCompletionsResult.class),
        @Type(GetPerspectiveDetailsResult.class),
        @Type(GetPerspectiveLayoutResult.class),
        @Type(GetPerspectivesResult.class),
        @Type(GetPossibleItemCompletionsResult.class),
        @Type(GetProjectChangesResult.class),
        @Type(GetProjectDetailsResult.class),
        @Type(GetProjectEventsResult.class),
        @Type(GetProjectFormDescriptorsResult.class),
        @Type(GetProjectInfoResult.class),
        @Type(GetPerspectiveLayoutResult.class),
        @Type(GetProjectPermissionsResult.class),
        @Type(GetProjectPrefixDeclarationsResult.class),
        @Type(GetProjectSettingsResult.class),
        @Type(GetProjectSharingSettingsResult.class),
        @Type(GetProjectTagsResult.class),
        @Type(GetRevisionSummariesResult.class),
        @Type(GetRootOntologyIdResult.class),
        @Type(GetSearchSettingsResult.class),
        @Type(GetUserIdCompletionsResult.class),
        @Type(GetUserProjectEntityGraphCriteriaResult.class),
        @Type(GetUsageResult.class),
        @Type(GetWatchesResult.class),
        @Type(GetWatchedEntityChangesResult.class),
        @Type(LoadProjectResult.class),
        @Type(LogOutUserResult.class),
        @Type(LookupEntitiesResult.class),
        @Type(MergeEntitiesResult.class),
        @Type(MergeUploadedProjectResult.class),
        @Type(MoveEntitiesToParentResult.class),
        @Type(MoveHierarchyNodeResult.class),
        @Type(MoveProjectsToTrashResult.class),
        @Type(NewOntologyMergeAddResult.class),
        @Type(PerformEntitySearchResult.class),
        @Type(PerformLoginResult.class),
        @Type(RebuildPermissionsResult.class),
        @Type(RemoveProjectFromTrashResult.class),
        @Type(ResetPasswordResult.class),
        @Type(ResetPerspectivesResult.class),
        @Type(ResetPerspectiveLayoutResult.class),
        @Type(ResetPerspectivesResult.class),
        @Type(SetPerspectivesResult.class),
        @Type(RevertRevisionResult.class),
        @Type(SetAnnotationValueResult.class),
        @Type(SetApplicationSettingsResult.class),
        @Type(SetDiscussionThreadStatusResult.class),
        @Type(SetEmailAddressResult.class),
        @Type(SetEntityCrudKitSettingsResult.class),
        @Type(SetEntityFormDescriptorResult.class),
        @Type(SetEntityFormDataResult.class),
        @Type(SetEntityGraphActiveFiltersResult.class),
        @Type(SetEntityWatchesResult.class),
        @Type(SetManchesterSyntaxFrameResult.class),
        @Type(SetOboTermCrossProductResult.class),
        @Type(SetOboTermDefinitionResult.class),
        @Type(SetOboTermIdResult.class),
        @Type(SetOboTermRelationshipsResult.class),
        @Type(SetOboTermSynonymsResult.class),
        @Type(SetOboTermXRefsResult.class),
        @Type(SetOntologyAnnotationsResult.class),
        @Type(SetPerspectiveLayoutResult.class),
        @Type(SetProjectFormDescriptorsResult.class),
        @Type(SetProjectPrefixDeclarationsResult.class),
        @Type(SetProjectSettingsResult.class),
        @Type(SetProjectSharingSettingsResult.class),
        @Type(SetProjectTagsResult.class),
        @Type(SetSearchSettingsResult.class),
        @Type(SetUserProjectEntityGraphSettingsResult.class),
        @Type(UpdateEntityTagsResult.class),
        @Type(UpdateFormDescriptorResult.class),
})
public interface Result extends IsSerializable {

}
