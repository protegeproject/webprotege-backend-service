package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.bulkop.EditAnnotationValuesActionHandler;
import edu.stanford.protege.webprotege.bulkop.MoveToParentActionHandler;
import edu.stanford.protege.webprotege.bulkop.SetAnnotationValueActionHandler;
import edu.stanford.protege.webprotege.change.GetProjectChangesActionHandler;
import edu.stanford.protege.webprotege.change.GetWatchedEntityChangesActionHandler;
import edu.stanford.protege.webprotege.change.RevertRevisionActionHandler;
import edu.stanford.protege.webprotege.crud.GetEntityCrudKitSettingsActionHandler;
import edu.stanford.protege.webprotege.crud.GetEntityCrudKitsActionHandler;
import edu.stanford.protege.webprotege.crud.SetEntityCrudKitSettingsActionHandler;
import edu.stanford.protege.webprotege.csv.ImportCSVFileActionHandler;
import edu.stanford.protege.webprotege.dispatch.ProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.entity.CreateEntityFromFormDataActionHandler;
import edu.stanford.protege.webprotege.entity.GetDeprecatedEntitiesActionHandler;
import edu.stanford.protege.webprotege.entity.LookupEntitiesActionHandler;
import edu.stanford.protege.webprotege.entity.MergeEntitiesActionHandler;
import edu.stanford.protege.webprotege.form.*;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.protege.webprotege.individuals.GetIndividualsActionHandler;
import edu.stanford.protege.webprotege.individuals.GetIndividualsPageContainingIndividualActionHandler;
import edu.stanford.protege.webprotege.issues.*;
import edu.stanford.protege.webprotege.lang.GetProjectLangTagsActionHandler;
import edu.stanford.protege.webprotege.mansyntax.render.GetEntityHtmlRenderingActionHandler;
import edu.stanford.protege.webprotege.mansyntax.render.GetEntityRenderingActionHandler;
import edu.stanford.protege.webprotege.match.GetMatchingEntitiesActionHandler;
import edu.stanford.protege.webprotege.merge.ComputeProjectMergeActionHandler;
import edu.stanford.protege.webprotege.merge.MergeUploadedProjectActionHandler;
import edu.stanford.protege.webprotege.merge_add.ExistingOntologyMergeAddActionHandler;
import edu.stanford.protege.webprotege.merge_add.GetAllOntologiesActionHandler;
import edu.stanford.protege.webprotege.merge_add.NewOntologyMergeAddActionHandler;
import edu.stanford.protege.webprotege.obo.*;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.GetProjectInfoActionHandler;
import edu.stanford.protege.webprotege.project.GetProjectPrefixDeclarationsActionHandler;
import edu.stanford.protege.webprotege.project.SetProjectPrefixDeclarationsActionHandler;
import edu.stanford.protege.webprotege.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.protege.webprotege.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.protege.webprotege.revision.GetHeadRevisionNumberActionHandler;
import edu.stanford.protege.webprotege.revision.GetRevisionSummariesActionHandler;
import edu.stanford.protege.webprotege.search.GetSearchSettingsActionHandler;
import edu.stanford.protege.webprotege.search.PerformEntitySearchActionHandler;
import edu.stanford.protege.webprotege.search.SetSearchSettingsActionHandler;
import edu.stanford.protege.webprotege.sharing.GetProjectSharingSettingsActionHandler;
import edu.stanford.protege.webprotege.sharing.SetProjectSharingSettingsActionHandler;
import edu.stanford.protege.webprotege.tag.GetEntityTagsActionHandler;
import edu.stanford.protege.webprotege.tag.GetProjectTagsActionHandler;
import edu.stanford.protege.webprotege.tag.SetProjectTagsActionHandler;
import edu.stanford.protege.webprotege.tag.UpdateEntityTagsActionHandler;
import edu.stanford.protege.webprotege.usage.GetEntityUsageActionHandler;
import edu.stanford.protege.webprotege.viz.GetEntityGraphActionHandler;
import edu.stanford.protege.webprotege.viz.GetUserProjectEntityGraphCriteriaActionHandler;
import edu.stanford.protege.webprotege.viz.SetEntityGraphActiveFiltersActionHandler;
import edu.stanford.protege.webprotege.viz.SetUserProjectEntityGraphCriteriaActionHandler;
import edu.stanford.protege.webprotege.watches.AddWatchActionHandler;
import edu.stanford.protege.webprotege.watches.GetWatchesActionHandler;
import edu.stanford.protege.webprotege.watches.RemoveWatchActionHandler;
import edu.stanford.protege.webprotege.watches.SetEntityWatchesActionHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
public class ProjectActionHandlersModule {

     
    public ProjectActionHandler provideGetProjectSettingsActionHandler(GetProjectSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetProjectSettingsActionHandler(SetProjectSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideUpdateClassFrameActionHandler(UpdateClassFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetObjectPropertyFrameActionHandler(GetObjectPropertyFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideUpdateObjectPropertyFrameHandler(UpdateObjectPropertyFrameHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideUpdateDataPropertyFrameHandler(UpdateDataPropertyFrameHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetAnnotationPropertyFrameActionHandler(
            GetAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideUpdateAnnotationPropertyFrameActionHandler(
            UpdateAnnotationPropertyFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetNamedIndividualFrameActionHandler(
            GetNamedIndividualFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetAllOntologiesActionHandler(GetAllOntologiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideUpdateNamedIndividualFrameHandler(UpdateNamedIndividualFrameHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetOntologyFramesActionHandler(GetOntologyFramesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetRootOntologyIdActionHandler(GetRootOntologyIdActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetOntologyAnnotationsActionHandler(
            GetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetOntologyAnnotationsActionHandler(
            SetOntologyAnnotationsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateClassesActionHandler(CreateClassesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateObjectPropertyActionHandler(CreateObjectPropertiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateDataPropertiesActionHandler(CreateDataPropertiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateAnnotationPropertiesActionHandler(
            CreateAnnotationPropertiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateNamedIndividualsActionHandler(
            CreateNamedIndividualsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideLookupEntitiesActionHandler(LookupEntitiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideAddWatchActionHandler(AddWatchActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideRemoveWatchActionHandler(RemoveWatchActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetEntityWatchesActionHandler(SetEntityWatchesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetWatchesActionHandler(GetWatchesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideImportCSVFileActionHandler(ImportCSVFileActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetUsageActionHandler(GetEntityUsageActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetIndividualsActionHandler(GetIndividualsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityRenderingActionHandler(GetEntityRenderingActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetDataPropertyFrameActionHandler(GetDataPropertyFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetEntityCrudKitSettingsActionHandler(
            SetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityCrudKitSettingsActionHandler(
            GetEntityCrudKitSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetManchesterSyntaxFrameActionHandler(
            GetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetManchesterSyntaxFrameActionHandler(
            SetManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCheckManchesterSyntaxFrameActionHandler(
            CheckManchesterSyntaxFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetManchesterSyntaxFrameCompletionsActionHandler(
            GetManchesterSyntaxFrameCompletionsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetProjectSharingSettingsActionHandler(
            GetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetProjectSharingSettingsActionHandler(
            SetProjectSharingSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetHeadRevisionNumberActionHandler(
            GetHeadRevisionNumberActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetRevisionSummariesActionHandler(GetRevisionSummariesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideComputeProjectMergeActionHandler(ComputeProjectMergeActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideNewOntologyMergeAddActionHandler(NewOntologyMergeAddActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideMergeUploadedProjectActionHandler(MergeUploadedProjectActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetProjectChangesActionHandler(GetProjectChangesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetWatchedEntityChangesActionHandler(
            GetWatchedEntityChangesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideRevertRevisionActionHandler(RevertRevisionActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetPerspectiveLayoutActionHandler(GetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetPerspectiveLayoutActionHandler(SetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetPerspectivesActionHandler(SetPerspectivesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetIssuesActionHandler(GetIssuesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetDiscussionThreadsActionHandler(GetEntityDiscussionThreadsHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateEntityDiscussionThreadActionHandler(CreateEntityDiscussionThreadHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideAddEntityCommentActionHandler(AddEntityCommentHandler handler) {
        return handler;
    }


     
    public ProjectActionHandler<DeleteEntityCommentAction, DeleteEntityCommentResult> provideDeleteEntityCommentActionHandler(DeleteEntityCommentHandler handler) {
        return handler;
    }


     
    public ProjectActionHandler provideEditCommentAction(EditCommentActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetDiscussionThreadStatusHandler(SetDiscussionThreadStatusHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetCommentedEntitiesActionHandler(GetCommentedEntitiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideResetPerspectiveLayoutActionHandler(ResetPerspectiveLayoutActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesPerformEntitySearchActionHandler(PerformEntitySearchActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesDeleteEntitiesActionHandler(DeleteEntitiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetDeprecatedEntitiesActionHandler(GetDeprecatedEntitiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetClassFrameActionHandler(GetClassFrameActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetOboTermIdActionHandler(GetOboTermIdActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetOboTermDefinitionActionHandler(GetOboTermDefinitionActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetOboNamespacesActionHandler(GetOboNamespacesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetOboTermIdActionHandler(SetOboTermIdActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetOboTermDefinitionActionHandler(SetOboTermDefinitionActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetOboTermRelationshipsActionHandler(GetOboTermRelationshipsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetOboTermCrossProductsActionHandler(GetOboTermCrossProductsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetOboTermCrossProductsActionHandler(SetOboTermCrossProductsActionHandler handler) {
        return handler;
    }


     
    public ProjectActionHandler providesGetOboTermSynonymsActionHandler(GetOboTermSynonymsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetOboTermSynonymsActionHandler(SetOboTermSynonymsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetOboTermXRefsActionHandler(GetOboTermXRefsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetOboTermXRefsActionHandler(SetOboTermXRefsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetClassHierarchyChildrenActionHandler(GetEntityHierarchyChildrenActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetHierarchyPathsToRootActionHandler(GetHierarchyPathsToRootActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetHierarchyRootsActionHandler(GetHierarchyRootsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesMoveHierarchyNodeActionHandler(MoveHierarchyNodeActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetProjectPrefixDeclarationsActionHandler(GetProjectPrefixDeclarationsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetProjectPrefixDeclarationsActionHandler(SetProjectPrefixDeclarationsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesMergeClassesActionHandler(MergeEntitiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetEntityTagsActionHandler(GetEntityTagsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesUpdateEntityTagsActionHandler(UpdateEntityTagsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetProjectTagsActionHandler(GetProjectTagsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetProjectTagsActionHandler(SetProjectTagsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesAddAxiomActionHandler(AddAxiomsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesDeleteAxiomActionHandler(DeleteAxiomsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetRevisionsActionHandler(GetRevisionsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetRevisionActionHandler(GetRevisionActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetMatchingEntitiesAction(GetMatchingEntitiesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetProjectInfoActionHandler(GetProjectInfoActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetHierarchySiblingsActionHandler(GetHierarchySiblingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetIndividualsPageContainingIndividualActionHandler(GetIndividualsPageContainingIndividualActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetAnnotationValueActionHandler(SetAnnotationValueActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideReplaceAnnotationValuesActionHandler(EditAnnotationValuesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideMoveToParentActionHandler(MoveToParentActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityDotRenderingActionHandler(GetEntityGraphActionHandler handler) {
        return handler;
    }


     
    public ProjectActionHandler providesGetEntityCrudKitsActionHandler(GetEntityCrudKitsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetEntityFormDataActionHandler(SetEntityFormsDataActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetUserProjectEntityGraphCriteriaActionHandler(
            GetUserProjectEntityGraphCriteriaActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetEntityFormActionHandler(GetEntityFormsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetUserProjectEntityGraphCriteriaActionHandler(
            SetUserProjectEntityGraphCriteriaActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesGetProjectFormDescriptorsActionHandler(GetProjectFormDescriptorsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler providesSetProjectFormDescriptorsActionHandler(SetProjectFormDescriptorsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetEntityGraphActiveFiltersActionHandler(SetEntityGraphActiveFiltersActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityFormDescriptorActionHandler(GetEntityFormDescriptorActionHandler actionHandler) {
        return actionHandler;
    }

     
    public ProjectActionHandler provideSetEntityFormDescriptorActionHandler(SetEntityFormDescriptorActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetFreshFormIdActionHandler(GetFreshFormIdActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityHtmlRenderingActionHandler(GetEntityHtmlRenderingActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCopyFormDescriptorsFromProjectActionHandler(CopyFormDescriptorsFromProjectActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideDeleteFormActionHandler(DeleteFormActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetProjectLangTagsActionHandler(GetProjectLangTagsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetSearchSettingsActionHandler(GetSearchSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideSetSearchSettingsActionHandler(SetSearchSettingsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideUpdateFormDescriptorActionHandler(UpdateFormDescriptorActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetPerspectiveDetailsActionHandler(GetPerspectiveDetailsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideResetPerspectivesActionHandler(ResetPerspectivesActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityCreationFormsActionHandler(GetEntityCreationFormsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideCreateEntityFromFormDataActionHandler(CreateEntityFromFormDataActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideGetEntityDeprecationFormsActionHandler(GetEntityDeprecationFormsActionHandler handler) {
        return handler;
    }

     
    public ProjectActionHandler provideDeprecateEntityByFormActionHandler(DeprecateEntityByFormActionHandler handler) {
        return handler;
    }

    public ProjectActionHandler providesExistingOntologyMergeAddActionHandler(ExistingOntologyMergeAddActionHandler handler){
        return handler;
    }
}
