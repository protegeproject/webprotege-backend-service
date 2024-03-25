package edu.stanford.protege.webprotege;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.axiom.AxiomComparatorImpl;
import edu.stanford.protege.webprotege.axiom.AxiomSubjectProvider;
import edu.stanford.protege.webprotege.bulkop.*;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.crud.*;
import edu.stanford.protege.webprotege.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.protege.webprotege.csv.ImportCSVFileActionHandler;
import edu.stanford.protege.webprotege.dispatch.handlers.*;
import edu.stanford.protege.webprotege.entity.*;
import edu.stanford.protege.webprotege.forms.*;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.frame.translator.AnnotationPropertyFrameTranslator;
import edu.stanford.protege.webprotege.frame.translator.DataPropertyFrameTranslator;
import edu.stanford.protege.webprotege.frame.translator.NamedIndividualFrameTranslator;
import edu.stanford.protege.webprotege.frame.translator.ObjectPropertyFrameTranslator;
import edu.stanford.protege.webprotege.hierarchy.*;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.individuals.CreateIndividualsChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.individuals.CreateNamedIndividualsActionHandler;
import edu.stanford.protege.webprotege.individuals.GetIndividualsActionHandler;
import edu.stanford.protege.webprotege.individuals.GetIndividualsPageContainingIndividualActionHandler;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.issues.*;
import edu.stanford.protege.webprotege.lang.ActiveLanguagesManager;
import edu.stanford.protege.webprotege.lang.GetProjectLangTagsActionHandler;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.protege.webprotege.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.protege.webprotege.mansyntax.render.*;
import edu.stanford.protege.webprotege.match.GetMatchingEntitiesActionHandler;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.match.MatchingEngine;
import edu.stanford.protege.webprotege.merge.*;
import edu.stanford.protege.webprotege.merge_add.ExistingOntologyMergeAddActionHandler;
import edu.stanford.protege.webprotege.merge_add.GetUploadedAndProjectOntologyIdsActionHandler;
import edu.stanford.protege.webprotege.merge_add.MergeOntologiesActionHandler;
import edu.stanford.protege.webprotege.perspective.*;
import edu.stanford.protege.webprotege.project.*;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import edu.stanford.protege.webprotege.projectsettings.GetProjectSettingsActionHandler;
import edu.stanford.protege.webprotege.projectsettings.SetProjectSettingsActionHandler;
import edu.stanford.protege.webprotege.renderer.ContextRenderer;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.protege.webprotege.revision.*;
import edu.stanford.protege.webprotege.search.*;
import edu.stanford.protege.webprotege.sharing.GetProjectSharingSettingsActionHandler;
import edu.stanford.protege.webprotege.sharing.ProjectSharingSettingsManager;
import edu.stanford.protege.webprotege.sharing.SetProjectSharingSettingsActionHandler;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import edu.stanford.protege.webprotege.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.protege.webprotege.tag.*;
import edu.stanford.protege.webprotege.usage.GetEntityUsageActionHandler;
import edu.stanford.protege.webprotege.usage.ReferencingAxiomVisitorFactory;
import edu.stanford.protege.webprotege.viz.*;
import edu.stanford.protege.webprotege.watches.GetWatchesActionHandler;
import edu.stanford.protege.webprotege.watches.SetWatchesActionHandler;
import edu.stanford.protege.webprotege.watches.WatchManager;
import edu.stanford.protege.webprotege.webhook.CommentPostedSlackWebhookInvoker;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.springframework.context.annotation.Bean;

import javax.inject.Provider;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-14
 */
public class ProjectActionHandlerBeansConfiguration {



    @Bean
    GetProjectSettingsActionHandler getProjectSettingsActionHandler(AccessManager p1,
                                                                    ProjectId p2,
                                                                    ProjectDetailsManager p3) {
        return new GetProjectSettingsActionHandler(p1, p2, p3);
    }


    @Bean
    SetProjectSettingsActionHandler setProjectSettingsActionHandler(AccessManager p1, ProjectDetailsManager p2) {
        return new SetProjectSettingsActionHandler(p1, p2);
    }


    @Bean
    UpdateClassFrameActionHandler updateClassFrameActionHandler(AccessManager p1,
                                                                HasApplyChanges p3,
                                                                FrameChangeGeneratorFactory p4,
                                                                PlainFrameRenderer plainFrameRenderer) {
        return new UpdateClassFrameActionHandler(p1, p3, p4, plainFrameRenderer);
    }


    @Bean
    GetObjectPropertyFrameActionHandler getObjectPropertyFrameActionHandler(AccessManager p1,
                                                                            Provider<ObjectPropertyFrameTranslator> p2,
                                                                            PlainFrameRenderer plainFrameRenderer) {
        return new GetObjectPropertyFrameActionHandler(p1, p2, plainFrameRenderer);
    }


    @Bean
    UpdateObjectPropertyFrameHandler updateObjectPropertyFrameHandler(AccessManager p1,
                                                                      HasApplyChanges p3,
                                                                      FrameChangeGeneratorFactory p4,
                                                                      FrameComponentSessionRenderer p5,
                                                                      Comparator<PropertyValue> p6,
                                                                      PlainFrameRenderer plainFrameRenderer) {
        return new UpdateObjectPropertyFrameHandler(p1, p3, p4, plainFrameRenderer);
    }


    @Bean
    UpdateDataPropertyFrameHandler updateDataPropertyFrameHandler(AccessManager p1,
                                                                  HasApplyChanges p3,
                                                                  FrameChangeGeneratorFactory p5,
                                                                  PlainFrameRenderer plainFrameRenderer) {
        return new UpdateDataPropertyFrameHandler(p1, p3, p5, plainFrameRenderer);
    }


    @Bean
    GetAnnotationPropertyFrameActionHandler getAnnotationPropertyFrameActionHandler(AccessManager p1,
                                                                                    Provider<AnnotationPropertyFrameTranslator> p3,
                                                                                    FrameComponentSessionRenderer p4,
                                                                                    Comparator<PropertyValue> p5,
                                                                                    PlainFrameRenderer plainFrameRenderer) {
        return new GetAnnotationPropertyFrameActionHandler(p1, p3, plainFrameRenderer);
    }
    
    @Bean
    UpdateAnnotationPropertyFrameActionHandler updateAnnotationPropertyFrameActionHandler(AccessManager p1,
                                                                                          HasApplyChanges p3,
                                                                                          FrameChangeGeneratorFactory p4,
                                                                                          FrameComponentSessionRenderer p5,
                                                                                          Comparator<PropertyValue> p6,
                                                                                          PlainFrameRenderer plainFrameRenderer) {
        return new UpdateAnnotationPropertyFrameActionHandler(p1, p3, p4, plainFrameRenderer);
    }


    @Bean
    GetNamedIndividualFrameActionHandler getNamedIndividualFrameActionHandler(AccessManager p1,
                                                                              Provider<NamedIndividualFrameTranslator> p2,
                                                                              FrameComponentSessionRendererFactory p3,
                                                                              Comparator<PropertyValue> p4,
                                                                              PlainFrameRenderer plainFrameRenderer) {
        return new GetNamedIndividualFrameActionHandler(p1, p2, plainFrameRenderer);
    }

    @Bean
    public UploadedOntologiesCache uploadedOntologiesCache() {
        return new UploadedOntologiesCache();
    }

    @Bean
    GetUploadedAndProjectOntologyIdsActionHandler getUploadedAndProjectOntologyIdsActionHandler(AccessManager p1,
                                                                                ProjectId p2,
                                                                                UploadedOntologiesCache p3,
                                                                                ProjectOntologiesBuilder p4) {
        return new GetUploadedAndProjectOntologyIdsActionHandler(p1, p2, p3, p4);
    }


    @Bean
    UpdateNamedIndividualFrameHandler updateNamedIndividualFrameHandler(AccessManager p1,
                                                                        HasApplyChanges p3,
                                                                        FrameChangeGeneratorFactory p4,
                                                                        FrameComponentSessionRenderer p5,
                                                                        Comparator<PropertyValue> p6,
                                                                        PlainFrameRenderer plainFrameRenderer) {
        return new UpdateNamedIndividualFrameHandler(p1, p3, p4, plainFrameRenderer);
    }


    @Bean
    GetOntologyFramesActionHandler getOntologyFramesActionHandler(AccessManager p1,
                                                                  ProjectOntologiesIndex p2,
                                                                  OntologyAnnotationsIndex p3,
                                                                  WebProtegeOntologyIRIShortFormProvider p4,
                                                                  ContextRenderer p5) {
        return new GetOntologyFramesActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetRootOntologyIdActionHandler getRootOntologyIdActionHandler(AccessManager p1,
                                                                  ProjectId p2,
                                                                  DefaultOntologyIdManager p3) {
        return new GetRootOntologyIdActionHandler(p1, p2, p3);
    }


    @Bean
    GetOntologyAnnotationsActionHandler getOntologyAnnotationsActionHandler(AccessManager p1,
                                                                            OntologyAnnotationsIndex p2,
                                                                            RenderingManager p3,
                                                                            PropertyValueComparator p4,
                                                                            DefaultOntologyIdManager p5) {
        return new GetOntologyAnnotationsActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    SetOntologyAnnotationsActionHandler setOntologyAnnotationsActionHandler(AccessManager p1,

                                                                            HasApplyChanges p3,
                                                                            OWLDataFactory p4,
                                                                            OntologyAnnotationsIndex p5) {
        return new SetOntologyAnnotationsActionHandler(p1, p3, p4, p5);
    }


    @Bean
    CreateClassesActionHandler createClassesActionHandler(AccessManager p1,

                                                          HasApplyChanges p3,
                                                          CreateClassesChangeGeneratorFactory p4,
                                                          EntityNodeRenderer p5) {
        return new CreateClassesActionHandler(p1, p3, p4, p5);
    }


    @Bean
    CreateObjectPropertiesActionHandler createObjectPropertyActionHandler(AccessManager p1,

                                                                          HasApplyChanges p3,
                                                                          ProjectId p4,
                                                                          CreateObjectPropertiesChangeGeneratorFactory p5,
                                                                          EntityNodeRenderer p6) {
        return new CreateObjectPropertiesActionHandler(p1, p3, p4, p5, p6);
    }


    @Bean
    CreateDataPropertiesActionHandler createDataPropertiesActionHandler(AccessManager p1,

                                                                        HasApplyChanges p3,
                                                                        ProjectId p4,
                                                                        CreateDataPropertiesChangeGeneratorFactory p5,
                                                                        EntityNodeRenderer p6) {
        return new CreateDataPropertiesActionHandler(p1, p3, p4, p5, p6);
    }


    @Bean
    CreateAnnotationPropertiesActionHandler createAnnotationPropertiesActionHandler(AccessManager p1,

                                                                                    HasApplyChanges p3,
                                                                                    ProjectId p4,
                                                                                    RenderingManager p5,
                                                                                    CreateAnnotationPropertiesChangeGeneratorFactory p6,
                                                                                    EntityNodeRenderer p7) {
        return new CreateAnnotationPropertiesActionHandler(p1, p3, p4, p5, p6, p7);
    }


    @Bean
    CreateNamedIndividualsActionHandler createNamedIndividualsActionHandler(AccessManager p1,
                                                                            ProjectId p2,
                                                                            HasApplyChanges p4,
                                                                            EntityNodeRenderer p5,
                                                                            CreateIndividualsChangeListGeneratorFactory p6) {
        return new CreateNamedIndividualsActionHandler(p1, p2, p4, p5, p6);
    }


    @Bean
    LookupEntitiesActionHandler lookupEntitiesActionHandler(AccessManager p1,
                                                            ProjectId p2,
                                                            PlaceUrl p3,
                                                            EntityNodeRenderer p4,
                                                            DictionaryManager p5,
                                                            LanguageManager p6, MatcherFactory p7) {
        return new LookupEntitiesActionHandler(p1, p2, p3, p4, p5, p6, p7);
    }

    @Bean
    SetWatchesActionHandler setEntityWatchesActionHandler(AccessManager p1,

                                                          WatchManager p3) {
        return new SetWatchesActionHandler(p1, p3);
    }


    @Bean
    GetWatchesActionHandler getWatchesActionHandler(AccessManager p1, WatchManager p2) {
        return new GetWatchesActionHandler(p1, p2);
    }


    @Bean
    ImportCSVFileActionHandler importCSVFileActionHandler() {
        return null;
    }


    @Bean
    GetEntityUsageActionHandler getUsageActionHandler(AccessManager p1,
                                                      ProjectId p2,
                                                      ProjectOntologiesIndex p3,
                                                      AxiomsByReferenceIndex p4,
                                                      EntityNodeRenderer p5,
                                                      ReferencingAxiomVisitorFactory p6) {
        return new GetEntityUsageActionHandler(p1, p2, p3, p4, p5, p6);
    }


    @Bean
    GetIndividualsActionHandler getIndividualsActionHandler(AccessManager p1,
                                                            ProjectId p2,
                                                            RenderingManager p3,
                                                            EntityNodeRenderer p4, IndividualsIndex p5) {
        return new GetIndividualsActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetEntityRenderingActionHandler getEntityRenderingActionHandler(AccessManager p1, RenderingManager p2) {
        return new GetEntityRenderingActionHandler(p1, p2);
    }


    @Bean
    GetDataPropertyFrameActionHandler getDataPropertyFrameActionHandler(AccessManager p1,
                                                                        Provider<DataPropertyFrameTranslator> p2,
                                                                        PlainFrameRenderer plainFrameRenderer) {
        return new GetDataPropertyFrameActionHandler(p1, p2, plainFrameRenderer);
    }


    @Bean
    SetEntityCrudKitSettingsActionHandler setEntityCrudKitSettingsActionHandler(AccessManager p1,
                                                                                ProjectId p2,
                                                                                ProjectEntityCrudKitSettingsRepository p3,
                                                                                HasApplyChanges p4,
                                                                                FindAndReplaceIRIPrefixChangeGeneratorFactory p5) {
        return new SetEntityCrudKitSettingsActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetEntityCrudKitSettingsActionHandler getEntityCrudKitSettingsActionHandler(AccessManager p1,
                                                                                ProjectEntityCrudKitHandlerCache p2) {
        return new GetEntityCrudKitSettingsActionHandler(p1, p2);
    }


    @Bean
    GetManchesterSyntaxFrameActionHandler getManchesterSyntaxFrameActionHandler(AccessManager p1,
                                                                                OntologyIRIShortFormProvider p2,
                                                                                DictionaryManager p3,
                                                                                RenderingManager p4,
                                                                                ProjectOntologiesIndex p5,
                                                                                OwlOntologyFacadeFactory p6) {
        return new GetManchesterSyntaxFrameActionHandler(p1, p2, p3, p4, p5, p6);
    }


    @Bean
    SetManchesterSyntaxFrameActionHandler setManchesterSyntaxFrameActionHandler(AccessManager p1,

                                                                                HasApplyChanges p3,
                                                                                GetManchesterSyntaxFrameActionHandler p4,
                                                                                RenderingManager p5,
                                                                                ManchesterSyntaxChangeGeneratorFactory p6) {
        return new SetManchesterSyntaxFrameActionHandler(p1, p3, p4, p5, p6);
    }


    @Bean
    CheckManchesterSyntaxFrameActionHandler checkManchesterSyntaxFrameActionHandler(AccessManager p1,
                                                                                    ManchesterSyntaxChangeGeneratorFactory p2,
                                                                                    RenderingManager p3) {
        return new CheckManchesterSyntaxFrameActionHandler(p1, p2, p3);
    }


    @Bean
    GetManchesterSyntaxFrameCompletionsActionHandler getManchesterSyntaxFrameCompletionsActionHandler(AccessManager p1,
                                                                                                      ProjectOntologiesIndex p2,
                                                                                                      DictionaryManager p3,
                                                                                                      WebProtegeOntologyIRIShortFormProvider p4,
                                                                                                      Provider<ManchesterSyntaxFrameParser> p5) {
        return new GetManchesterSyntaxFrameCompletionsActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetProjectSharingSettingsActionHandler getProjectSharingSettingsActionHandler(ProjectSharingSettingsManager p1,
                                                                                  AccessManager p2) {
        return new GetProjectSharingSettingsActionHandler(p1, p2);
    }


    @Bean
    SetProjectSharingSettingsActionHandler setProjectSharingSettingsActionHandler(AccessManager p1,
                                                                                  ProjectSharingSettingsManager p2) {
        return new SetProjectSharingSettingsActionHandler(p1, p2);
    }


    @Bean
    GetHeadRevisionNumberActionHandler getHeadRevisionNumberActionHandler(AccessManager p1, RevisionManager p2) {
        return new GetHeadRevisionNumberActionHandler(p1, p2);
    }


    @Bean
    GetRevisionSummariesActionHandler getRevisionSummariesActionHandler(AccessManager p1, RevisionManager p2) {
        return new GetRevisionSummariesActionHandler(p1, p2);
    }


    @Bean
    ComputeProjectMergeActionHandler computeProjectMergeActionHandler(AccessManager p1,
                                                                      ProjectId p2,
                                                                      AxiomComparatorImpl p3,
                                                                      LanguageManager p4,
                                                                      ProjectOntologiesBuilder p5,
                                                                      UploadedOntologiesCache p6) {
        return new ComputeProjectMergeActionHandler(p1, p2, p3, p4, p5, p6);
    }


    @Bean
    MergeOntologiesActionHandler newOntologyMergeAddActionHandler(AccessManager p1,
                                                                  ProjectOntologiesBuilder p2,
                                                                  UploadedOntologiesCache p3,
                                                                  HasApplyChanges p4) {
        return new MergeOntologiesActionHandler(p1, p2, p3, p4);
    }


    @Bean
    MergeUploadedProjectActionHandler mergeUploadedProjectActionHandler(AccessManager p1,
                                                                        OntologyPatcher p2,
                                                                        ProjectOntologiesBuilder p3,
                                                                        UploadedOntologiesCache p4,
                                                                        ModifiedProjectOntologiesCalculatorFactory p5) {
        return new MergeUploadedProjectActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetProjectChangesActionHandler getProjectChangesActionHandler(AccessManager p1, ProjectChangesManager p2) {
        return new GetProjectChangesActionHandler(p1, p2);
    }

    @Bean
    RevertRevisionActionHandler revertRevisionActionHandler(AccessManager p1,

                                                            HasApplyChanges p3,
                                                            ProjectId p4,
                                                            RevisionReverterChangeListGeneratorFactory p5) {
        return new RevertRevisionActionHandler(p1, p3, p4, p5);
    }


    @Bean
    GetPerspectiveLayoutActionHandler getPerspectiveLayoutActionHandler(PerspectivesManager p1) {
        return new GetPerspectiveLayoutActionHandler(p1);
    }


    @Bean
    SetPerspectiveLayoutActionHandler setPerspectiveLayoutActionHandler(AccessManager p1, PerspectivesManager p2) {
        return new SetPerspectiveLayoutActionHandler(p1, p2);
    }


    @Bean
    SetPerspectivesActionHandler setPerspectivesActionHandler(AccessManager p1, PerspectivesManager p2) {
        return new SetPerspectivesActionHandler(p1, p2);
    }

    @Bean
    GetEntityDiscussionThreadsHandler getDiscussionThreadsActionHandler(EntityDiscussionThreadRepository p1,
                                                                        AccessManager p2, RenderingManager p3) {
        return new GetEntityDiscussionThreadsHandler(p1, p2, p3);
    }


    @Bean
    CreateEntityDiscussionThreadHandler createEntityDiscussionThreadActionHandler(AccessManager p1,
                                                                                  ProjectId p2,
                                                                                  EntityDiscussionThreadRepository p3,
                                                                                  ProjectDetailsRepository p4,
                                                                                  CommentNotificationEmailer p5,
                                                                                  CommentPostedSlackWebhookInvoker p6,
                                                                                  HasGetRendering p8,
                                                                                  EventDispatcher eventDispatcher) {
        return new CreateEntityDiscussionThreadHandler(p1, p2, p3, p4, p5, p6, p8, eventDispatcher);
    }


    @Bean
    AddCommentHandler addEntityCommentActionHandler(ProjectId p1,
                                                    HasGetRendering p2,
                                                    EventDispatcher p3,
                                                    EntityDiscussionThreadRepository p4,
                                                    CommentNotificationEmailer p5,
                                                    CommentPostedSlackWebhookInvoker p6,
                                                    ProjectDetailsRepository p7, AccessManager p8) {
        return new AddCommentHandler(p1, p2, p3, p4, p5, p6, p7, p8);
    }



    @Bean
    DeleteEntityCommentHandler deleteEntityCommentActionHandler(EntityDiscussionThreadRepository p1) {
        return new DeleteEntityCommentHandler(p1);
    }



    @Bean
    EditCommentActionHandler UpdateCommentAction(AccessManager p1,
                                                 ProjectId p2,
                                                 EntityDiscussionThreadRepository p3,
                                                 EventDispatcher eventDispatcher) {
        return new EditCommentActionHandler(p1, p2, p3, eventDispatcher);
    }


    @Bean
    SetDiscussionThreadStatusHandler setDiscussionThreadStatusHandler(AccessManager p1,
                                                                      EntityDiscussionThreadRepository p2,
                                                                      ProjectId p4, EventDispatcher eventDispatcher) {
        return new SetDiscussionThreadStatusHandler(p1, p2, p4, eventDispatcher);
    }


    @Bean
    GetCommentedEntitiesActionHandler getCommentedEntitiesActionHandler(AccessManager p1,
                                                                        EntityDiscussionThreadRepository p2,
                                                                        HasGetRendering p3,
                                                                        EntitiesInProjectSignatureIndex p4) {
        return new GetCommentedEntitiesActionHandler(p1, p2, p3, p4);
    }

    @Bean
    ResetPerspectiveLayoutActionHandler resetPerspectiveLayoutActionHandler(AccessManager p1, PerspectivesManager p2) {
        return new ResetPerspectiveLayoutActionHandler(p1, p2);
    }


    @Bean
    PerformEntitySearchActionHandler sPerformEntitySearchActionHandler(AccessManager p1,
                                                                       EntitySearcherFactory p2,
                                                                       LanguageManager p3) {
        return new PerformEntitySearchActionHandler(p1, p2, p3);
    }


    @Bean
    DeleteEntitiesActionHandler deleteEntitiesActionHandler(AccessManager p1,

                                                            HasApplyChanges p3,
                                                            DeleteEntitiesChangeListGeneratorFactory p4) {
        return new DeleteEntitiesActionHandler(p1, p3, p4);
    }


    @Bean
    GetDeprecatedEntitiesActionHandler getDeprecatedEntitiesActionHandler(AccessManager p1,
                                                                          RenderingManager p2,
                                                                          DeprecatedEntitiesIndex p3) {
        return new GetDeprecatedEntitiesActionHandler(p1, p2, p3);
    }


    @Bean
    GetClassFrameActionHandler getClassFrameActionHandler(AccessManager p1,
                                                          ClassFrameProvider p2,
                                                          PlainFrameRenderer plainFrameRenderer) {
        return new GetClassFrameActionHandler(p1, p2, plainFrameRenderer);
    }

    @Bean
    GetEntityHierarchyChildrenActionHandler getClassHierarchyChildrenActionHandler(AccessManager p1,
                                                                                   HierarchyProviderMapper p2,
                                                                                   DeprecatedEntityChecker p3,
                                                                                   GraphNodeRenderer p4,
                                                                                   DictionaryManager p5) {
        return new GetEntityHierarchyChildrenActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetHierarchyPathsToRootActionHandler getHierarchyPathsToRootActionHandler(AccessManager p1,
                                                                              HierarchyProviderMapper p2,
                                                                              GraphNodeRenderer p3) {
        return new GetHierarchyPathsToRootActionHandler(p1, p2, p3);
    }


    @Bean
    GetHierarchyRootsActionHandler getHierarchyRootsActionHandler(AccessManager p1,
                                                                  HierarchyProviderMapper p2,
                                                                  EntityNodeRenderer p3) {
        return new GetHierarchyRootsActionHandler(p1, p2, p3);
    }


    @Bean
    MoveHierarchyNodeActionHandler sMoveHierarchyNodeActionHandler(AccessManager p1,

                                                                   HasApplyChanges p3,
                                                                   MoveEntityChangeListGeneratorFactory p4) {
        return new MoveHierarchyNodeActionHandler(p1, p3, p4);
    }


    @Bean
    GetProjectPrefixDeclarationsActionHandler getProjectPrefixDeclarationsActionHandler(AccessManager p1,
                                                                                        PrefixDeclarationsStore p2) {
        return new GetProjectPrefixDeclarationsActionHandler(p1, p2);
    }


    @Bean
    SetProjectPrefixDeclarationsActionHandler setProjectPrefixDeclarationsActionHandler(AccessManager p1,
                                                                                        PrefixDeclarationsStore p2) {
        return new SetProjectPrefixDeclarationsActionHandler(p1, p2);
    }


    @Bean
    MergeEntitiesActionHandler mergeClassesActionHandler(AccessManager p1,

                                                         HasApplyChanges p3,
                                                         MergeEntitiesChangeListGeneratorFactory p4) {
        return new MergeEntitiesActionHandler(p1, p3, p4);
    }


    @Bean
    GetEntityTagsActionHandler getEntityTagsActionHandler(AccessManager p1, TagsManager p2) {
        return new GetEntityTagsActionHandler(p1, p2);
    }


    @Bean
    UpdateEntityTagsActionHandler updateEntityTagsActionHandler(AccessManager p1,

                                                                TagsManager p3) {
        return new UpdateEntityTagsActionHandler(p1, p3);
    }


    @Bean
    GetProjectTagsActionHandler getProjectTagsActionHandler(AccessManager p1, TagsManager p2) {
        return new GetProjectTagsActionHandler(p1, p2);
    }


    @Bean
    SetProjectTagsActionHandler setProjectTagsActionHandler(AccessManager p1, TagsManager p2) {
        return new SetProjectTagsActionHandler(p1, p2);
    }


    @Bean
    AddAxiomsActionHandler addAxiomActionHandler(AccessManager p1,
                                                  ProjectId p2,
                                                  ChangeManager p3,
                                                  AddAxiomsChangeListGeneratorFactory p4) {
        return new AddAxiomsActionHandler(p1, p2, p3, p4);
    }

    @Bean
    AddAxiomsChangeListGeneratorFactory addAxiomsChangeListGeneratorFactory(DefaultOntologyIdManager p1) {
        return new AddAxiomsChangeListGeneratorFactory(p1);
    }

    @Bean
    SetEntityDeprecatedDelegateHandler setEntityDeprecatedDelegateHandler(AccessManager p1,
                                                                          ChangeManager p2,
                                                                          OWLDataFactory p3,
                                                                          DefaultOntologyIdManager p4) {
        return new SetEntityDeprecatedDelegateHandler(p1, p2, p3, p4);
    }

    @Bean
    DeleteAxiomsActionHandler deleteAxiomActionHandler(AccessManager p1,
                                                       ProjectId p2,
                                                       ChangeManager p3,
                                                       DefaultOntologyIdManager p4) {
        return new DeleteAxiomsActionHandler(p1, p2, p3, p4);
    }


    @Bean
    GetRevisionsActionHandler getRevisionsActionHandler(AccessManager p1,
                                                        RevisionManager p2,
                                                        RevisionDetailsExtractor p3) {
        return new GetRevisionsActionHandler(p1, p2, p3);
    }


    @Bean
    GetRevisionActionHandler getRevisionActionHandler(AccessManager p1, RevisionManager p2, RevisionDetailsExtractor p3) {
        return new GetRevisionActionHandler(p1, p2, p3);
    }


    @Bean
    GetMatchingEntitiesActionHandler getMatchingEntitiesAction(AccessManager p1,
                                                               DictionaryManager p2,
                                                               EntityNodeRenderer p3,
                                                               RenderingManager p4,
                                                               MatchingEngine p5) {
        return new GetMatchingEntitiesActionHandler(p1, p2, p3, p4, p5);
    }


    @Bean
    GetProjectInfoActionHandler getProjectInfoActionHandler(AccessManager p1,
                                                            ProjectDetailsManager p2,
                                                            ActiveLanguagesManager p3) {
        return new GetProjectInfoActionHandler(p1, p2, p3);
    }


    @Bean
    GetHierarchySiblingsActionHandler getHierarchySiblingsActionHandler(AccessManager p1,
                                                                        HierarchyProviderMapper p2,
                                                                        GraphNodeRenderer p3, DictionaryManager p4) {
        return new GetHierarchySiblingsActionHandler(p1, p2, p3, p4);
    }


    @Bean
    GetIndividualsPageContainingIndividualActionHandler getIndividualsPageContainingIndividualActionHandler(
            AccessManager p1, IndividualsIndex p2, EntityNodeRenderer p3) {
        return new GetIndividualsPageContainingIndividualActionHandler(p1, p2, p3);
    }


    @Bean
    SetAnnotationValueActionHandler setAnnotationValueActionHandler(AccessManager p1,

                                                                    HasApplyChanges p3,
                                                                    SetAnnotationValueActionChangeListGeneratorFactory p4) {
        return new SetAnnotationValueActionHandler(p1, p3, p4);
    }


    @Bean
    EditAnnotationValuesActionHandler replaceAnnotationValuesActionHandler(AccessManager p1,

                                                                           HasApplyChanges p3,
                                                                           EditAnnotationsChangeListGeneratorFactory p4) {
        return new EditAnnotationValuesActionHandler(p1, p3, p4);
    }


    @Bean
    MoveToParentActionHandler moveToParentActionHandler(AccessManager p1,

                                                        HasApplyChanges p3, MoveClassesChangeListGeneratorFactory p4) {
        return new MoveToParentActionHandler(p1, p3, p4);
    }


    @Bean
    GetEntityGraphActionHandler getEntityDotRenderingActionHandler(AccessManager p1,
                                                                   EntityGraphBuilderFactory p2,
                                                                   EdgeMatcherFactory p3,
                                                                   EntityGraphSettingsRepository p4, ObjectMapper p5) {
        return new GetEntityGraphActionHandler(p1, p2, p3, p4, p5);
    }



    @Bean
    GetEntityCrudKitsActionHandler getEntityCrudKitsActionHandler(EntityCrudKitRegistry p1,
                                                                  ProjectEntityCrudKitHandlerCache p2) {
        return new GetEntityCrudKitsActionHandler(p1, p2);
    }


    @Bean
    SetEntityFormsDataActionHandler setEntityFormDataActionHandler(AccessManager p1,

                                                                   HasApplyChanges p3,
                                                                   EntityFormChangeListGeneratorFactory p4) {
        return new SetEntityFormsDataActionHandler(p1, p3, p4);
    }


    @Bean
    GetUserProjectEntityGraphCriteriaActionHandler getUserProjectEntityGraphCriteriaActionHandler(AccessManager p1,
                                                                                                  EntityGraphSettingsRepository p2) {
        return new GetUserProjectEntityGraphCriteriaActionHandler(p1, p2);
    }


    @Bean
    GetEntityFormsActionHandler getEntityFormActionHandler(AccessManager p1,
                                                           ProjectId p2,
                                                           EntityFormManager p3,
                                                           RenderingManager p4) {
        return new GetEntityFormsActionHandler(p1, p2, p3, p4);
    }


    @Bean
    SetUserProjectEntityGraphCriteriaActionHandler setUserProjectEntityGraphCriteriaActionHandler(AccessManager p1,
                                                                                                  EntityGraphSettingsRepository p2) {
        return new SetUserProjectEntityGraphCriteriaActionHandler(p1, p2);
    }


    @Bean
    GetProjectFormDescriptorsActionHandler getProjectFormDescriptorsActionHandler(AccessManager p1,
                                                                                  EntityFormRepository p2,
                                                                                  EntityFormSelectorRepository p3) {
        return new GetProjectFormDescriptorsActionHandler(p1, p2, p3);
    }


    @Bean
    SetProjectFormDescriptorsActionHandler setProjectFormDescriptorsActionHandler(AccessManager p1,
                                                                                  EntityFormRepository p2) {
        return new SetProjectFormDescriptorsActionHandler(p1, p2);
    }


    @Bean
    SetEntityGraphActiveFiltersActionHandler setEntityGraphActiveFiltersActionHandler(AccessManager p1,
                                                                                      EntityGraphSettingsRepository p2) {
        return new SetEntityGraphActiveFiltersActionHandler(p1, p2);
    }


    @Bean
    GetEntityFormDescriptorActionHandler getEntityFormDescriptorActionHandler(AccessManager p1,
                                                                              ProjectId p2,
                                                                              EntityFormSelectorRepository p3,
                                                                              EntityFormRepository p4) {
        return new GetEntityFormDescriptorActionHandler(p1, p2, p3, p4);
    }


    @Bean
    SetEntityFormDescriptorActionHandler setEntityFormDescriptorActionHandler(AccessManager p1,
                                                                              EntityFormRepository p2,
                                                                              EntityFormSelectorRepository p3) {
        return new SetEntityFormDescriptorActionHandler(p1, p2, p3);
    }

    @Bean
    GetEntityHtmlRenderingActionHandler getEntityHtmlRenderingActionHandler(AccessManager p1,
                                                                            ManchesterSyntaxEntityFrameRenderer p2,
                                                                            RenderingManager p3) {
        return new GetEntityHtmlRenderingActionHandler(p1, p2, p3);
    }


    @Bean
    CopyFormDescriptorsActionHandler copyFormDescriptorsFromProjectActionHandler(AccessManager p1,
                                                                                 FormsCopierFactory p2) {
        return new CopyFormDescriptorsActionHandler(p1, p2);
    }


    @Bean
    DeleteFormActionHandler deleteFormActionHandler(AccessManager p1, EntityFormRepository p2) {
        return new DeleteFormActionHandler(p1, p2);
    }


    @Bean
    GetProjectLangTagsActionHandler getProjectLangTagsActionHandler(AccessManager p1, LanguageManager p2) {
        return new GetProjectLangTagsActionHandler(p1, p2);
    }


    @Bean
    GetSearchSettingsActionHandler getSearchSettingsActionHandler(AccessManager p1, EntitySearchFilterRepository p2) {
        return new GetSearchSettingsActionHandler(p1, p2);
    }


    @Bean
    SetSearchSettingsActionHandler setSearchSettingsActionHandler(AccessManager p1,
                                                                  ProjectEntitySearchFiltersManager p2) {
        return new SetSearchSettingsActionHandler(p1, p2);
    }


    @Bean
    UpdateFormDescriptorActionHandler updateFormDescriptorActionHandler(AccessManager p1, EntityFormRepository p2) {
        return new UpdateFormDescriptorActionHandler(p1, p2);
    }


    @Bean
    GetPerspectiveDetailsActionHandler getPerspectiveDetailsActionHandler(AccessManager p1, PerspectivesManager p2) {
        return new GetPerspectiveDetailsActionHandler(p1, p2);
    }


    @Bean
    ResetPerspectivesActionHandler resetPerspectivesActionHandler(AccessManager p1, PerspectivesManager p2) {
        return new ResetPerspectivesActionHandler(p1, p2);
    }


    @Bean
    GetEntityCreationFormsActionHandler getEntityCreationFormsActionHandler(AccessManager p1,
                                                                            EntityFormManager p2) {
        return new GetEntityCreationFormsActionHandler(p1, p2);
    }


    @Bean
    CreateEntityFromFormDataActionHandler createEntityFromFormDataActionHandler(AccessManager p1,

                                                                                HasApplyChanges p3,
                                                                                CreateEntityFromFormDataChangeListGeneratorFactory p4,
                                                                                RenderingManager p5) {
        return new CreateEntityFromFormDataActionHandler(p1, p3, p4, p5);
    }


    @Bean
    GetEntityDeprecationFormsActionHandler getEntityDeprecationFormsActionHandler(ProjectId p1,
                                                                                  AccessManager p2,
                                                                                  EntityFormManager p3,
                                                                                  AxiomsByReferenceIndex p4,
                                                                                  ProjectOntologiesIndex p5,
                                                                                  AxiomSubjectProvider p6,
                                                                                  ProjectDetailsManager p7) {
        return new GetEntityDeprecationFormsActionHandler(p1, p2, p3, p4, p5, p6, p7);
    }


    @Bean
    DeprecateEntityByFormActionHandler deprecateEntityByFormActionHandler(AccessManager p1,

                                                                          HasApplyChanges p3,
                                                                          ProjectId p4,
                                                                          DeprecateEntityByFormChangeListGeneratorFactory p5,
                                                                          ProjectDetailsRepository p6) {
        return new DeprecateEntityByFormActionHandler(p1, p3, p4, p5, p6);
    }

    @Bean
    ExistingOntologyMergeAddActionHandler sExistingOntologyMergeAddActionHandler(AccessManager p1,
                                                                                 ProjectId p2,
                                                                                 UploadedOntologiesCache p3,
                                                                                 ProjectOntologiesBuilder p4,
                                                                                 HasApplyChanges p5){
        return new ExistingOntologyMergeAddActionHandler(p1, p2, p3, p4, p5);
    }
}
