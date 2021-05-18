package edu.stanford.bmir.protege.web.server.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import edu.stanford.bmir.protege.web.server.app.GetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.server.app.SetApplicationSettingsResult;
import edu.stanford.bmir.protege.web.server.auth.ChangePasswordResult;
import edu.stanford.bmir.protege.web.server.auth.PerformLoginResult;
import edu.stanford.bmir.protege.web.server.bulkop.EditAnnotationsResult;
import edu.stanford.bmir.protege.web.server.bulkop.MoveEntitiesToParentResult;
import edu.stanford.bmir.protege.web.server.bulkop.SetAnnotationValueResult;
import edu.stanford.bmir.protege.web.server.change.GetProjectChangesResult;
import edu.stanford.bmir.protege.web.server.change.GetWatchedEntityChangesResult;
import edu.stanford.bmir.protege.web.server.change.RevertRevisionResult;
import edu.stanford.bmir.protege.web.server.chgpwd.ResetPasswordResult;
import edu.stanford.bmir.protege.web.server.crud.GetEntityCrudKitsResult;
import edu.stanford.bmir.protege.web.server.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.bmir.protege.web.server.entity.*;
import edu.stanford.bmir.protege.web.server.event.GetProjectEventsResult;
import edu.stanford.bmir.protege.web.server.form.*;
import edu.stanford.bmir.protege.web.server.frame.*;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.bmir.protege.web.server.individuals.GetIndividualsResult;
import edu.stanford.bmir.protege.web.server.issues.*;
import edu.stanford.bmir.protege.web.server.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.bmir.protege.web.server.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.bmir.protege.web.server.itemlist.GetUserIdCompletionsResult;
import edu.stanford.bmir.protege.web.server.lang.GetProjectLangTagsResult;
import edu.stanford.bmir.protege.web.server.mail.GetEmailAddressResult;
import edu.stanford.bmir.protege.web.server.mail.SetEmailAddressResult;
import edu.stanford.bmir.protege.web.server.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.server.merge.ComputeProjectMergeResult;
import edu.stanford.bmir.protege.web.server.merge.MergeUploadedProjectResult;
import edu.stanford.bmir.protege.web.server.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.server.merge_add.GetAllOntologiesResult;
import edu.stanford.bmir.protege.web.server.merge_add.NewOntologyMergeAddResult;
import edu.stanford.bmir.protege.web.server.obo.*;
import edu.stanford.bmir.protege.web.server.ontology.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.ontology.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.server.ontology.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.permissions.GetProjectPermissionsResult;
import edu.stanford.bmir.protege.web.server.permissions.RebuildPermissionsResult;
import edu.stanford.bmir.protege.web.server.perspective.*;
import edu.stanford.bmir.protege.web.server.project.*;
import edu.stanford.bmir.protege.web.server.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.server.projectsettings.SetProjectSettingsResult;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.bmir.protege.web.server.renderer.GetEntityRenderingResult;
import edu.stanford.bmir.protege.web.server.revision.GetHeadRevisionNumberResult;
import edu.stanford.bmir.protege.web.server.revision.GetRevisionSummariesResult;
import edu.stanford.bmir.protege.web.server.search.GetSearchSettingsResult;
import edu.stanford.bmir.protege.web.server.search.PerformEntitySearchResult;
import edu.stanford.bmir.protege.web.server.search.SetSearchSettingsResult;
import edu.stanford.bmir.protege.web.server.sharing.GetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.server.sharing.SetProjectSharingSettingsResult;
import edu.stanford.bmir.protege.web.server.tag.*;
import edu.stanford.bmir.protege.web.server.usage.GetUsageResult;
import edu.stanford.bmir.protege.web.server.user.CreateUserAccountResult;
import edu.stanford.bmir.protege.web.server.user.GetCurrentUserInSessionResult;
import edu.stanford.bmir.protege.web.server.user.LogOutUserResult;
import edu.stanford.bmir.protege.web.server.viz.GetEntityGraphResult;
import edu.stanford.bmir.protege.web.server.viz.GetUserProjectEntityGraphCriteriaResult;
import edu.stanford.bmir.protege.web.server.viz.SetEntityGraphActiveFiltersResult;
import edu.stanford.bmir.protege.web.server.viz.SetUserProjectEntityGraphSettingsResult;
import edu.stanford.bmir.protege.web.server.watches.GetWatchesResult;
import edu.stanford.bmir.protege.web.server.watches.SetEntityWatchesResult;


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
        @Type(GetClassFrameResult.class),
        @Type(GetDataPropertyFrameResult.class),
        @Type(GetAnnotationPropertyFrameResult.class),
        @Type(GetDataPropertyFrameResult.class),
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
        @Type(GetProjectLangTagsResult.class),
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
public interface Result {

}
