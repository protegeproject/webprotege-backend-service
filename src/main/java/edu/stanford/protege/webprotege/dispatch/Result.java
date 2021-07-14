package edu.stanford.protege.webprotege.dispatch;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.stanford.protege.webprotege.app.GetApplicationSettingsResult;
import edu.stanford.protege.webprotege.app.SetApplicationSettingsResult;
import edu.stanford.protege.webprotege.auth.ChangePasswordResult;
import edu.stanford.protege.webprotege.auth.PerformLoginResult;
import edu.stanford.protege.webprotege.bulkop.EditAnnotationsResult;
import edu.stanford.protege.webprotege.bulkop.MoveEntitiesToParentResult;
import edu.stanford.protege.webprotege.bulkop.SetAnnotationValueResult;
import edu.stanford.protege.webprotege.change.GetProjectChangesResult;
import edu.stanford.protege.webprotege.change.GetWatchedEntityChangesResult;
import edu.stanford.protege.webprotege.change.RevertRevisionResult;
import edu.stanford.protege.webprotege.chgpwd.ResetPasswordResult;
import edu.stanford.protege.webprotege.crud.GetEntityCrudKitsResult;
import edu.stanford.protege.webprotege.crud.SetEntityCrudKitSettingsResult;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.event.GetProjectEventsResult;
import edu.stanford.protege.webprotege.form.*;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.protege.webprotege.individuals.GetIndividualsResult;
import edu.stanford.protege.webprotege.issues.*;
import edu.stanford.protege.webprotege.itemlist.GetPersonIdCompletionsResult;
import edu.stanford.protege.webprotege.itemlist.GetPossibleItemCompletionsResult;
import edu.stanford.protege.webprotege.itemlist.GetUserIdCompletionsResult;
import edu.stanford.protege.webprotege.lang.GetProjectLangTagsResult;
import edu.stanford.protege.webprotege.mail.GetEmailAddressResult;
import edu.stanford.protege.webprotege.mail.SetEmailAddressResult;
import edu.stanford.protege.webprotege.match.GetMatchingEntitiesResult;
import edu.stanford.protege.webprotege.merge.ComputeProjectMergeResult;
import edu.stanford.protege.webprotege.merge.MergeUploadedProjectResult;
import edu.stanford.protege.webprotege.merge_add.ExistingOntologyMergeAddResult;
import edu.stanford.protege.webprotege.merge_add.GetAllOntologiesResult;
import edu.stanford.protege.webprotege.merge_add.NewOntologyMergeAddResult;
import edu.stanford.protege.webprotege.obo.*;
import edu.stanford.protege.webprotege.ontology.GetOntologyAnnotationsResult;
import edu.stanford.protege.webprotege.ontology.GetRootOntologyIdResult;
import edu.stanford.protege.webprotege.ontology.SetOntologyAnnotationsResult;
import edu.stanford.protege.webprotege.permissions.GetProjectPermissionsResult;
import edu.stanford.protege.webprotege.permissions.RebuildPermissionsResult;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.projectsettings.GetProjectSettingsResult;
import edu.stanford.protege.webprotege.projectsettings.SetProjectSettingsResult;
import edu.stanford.protege.webprotege.renderer.GetEntityHtmlRenderingResult;
import edu.stanford.protege.webprotege.renderer.GetEntityRenderingResult;
import edu.stanford.protege.webprotege.revision.GetHeadRevisionNumberResult;
import edu.stanford.protege.webprotege.revision.GetRevisionSummariesResult;
import edu.stanford.protege.webprotege.search.GetSearchSettingsResult;
import edu.stanford.protege.webprotege.search.PerformEntitySearchResult;
import edu.stanford.protege.webprotege.search.SetSearchSettingsResult;
import edu.stanford.protege.webprotege.sharing.GetProjectSharingSettingsResult;
import edu.stanford.protege.webprotege.sharing.SetProjectSharingSettingsResult;
import edu.stanford.protege.webprotege.tag.*;
import edu.stanford.protege.webprotege.usage.GetUsageResult;
import edu.stanford.protege.webprotege.user.CreateUserAccountResult;
import edu.stanford.protege.webprotege.user.GetCurrentUserInSessionResult;
import edu.stanford.protege.webprotege.user.LogOutUserResult;
import edu.stanford.protege.webprotege.viz.GetEntityGraphResult;
import edu.stanford.protege.webprotege.viz.GetUserProjectEntityGraphCriteriaResult;
import edu.stanford.protege.webprotege.viz.SetEntityGraphActiveFiltersResult;
import edu.stanford.protege.webprotege.viz.SetUserProjectEntityGraphSettingsResult;
import edu.stanford.protege.webprotege.watches.GetWatchesResult;
import edu.stanford.protege.webprotege.watches.SetEntityWatchesResult;


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
