package edu.stanford.protege.webprotege.entity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2017
 */
public class ChangeEntityIRIActionHandler { //extends AbstractProjectActionHandler<ChangeEntityIRIAction, ChangeEntityIRIResult> {


//    @Nonnull
//    private final ProjectId projectId;
//
//    @Nonnull
//    private final RenderingManager renderer;
//
//    @Nonnull
//    private final OWLDataFactory dataFactory;
//
//    @Nonnull
//    private final HasApplyChanges applyChanges;
//
//    @Nonnull
//    private final EntityRenamer entityRenamer;
//
//    @Nonnull
//    private final EntityDiscussionThreadRepository discussionThreadRepository;
//
//
//    @Inject
//    public ChangeEntityIRIActionHandler(@Nonnull AccessManager accessManager,
//                                        @Nonnull ProjectId projectId,
//                                        @Nonnull RenderingManager renderer,
//                                        @Nonnull OWLDataFactory dataFactory,
//                                        @Nonnull HasApplyChanges applyChanges,
//                                        @Nonnull EntityRenamer entityRenamer,
//                                        @Nonnull EntityDiscussionThreadRepository discussionThreadRepository) {
//        super(accessManager);
//        this.projectId = projectId;
//        this.entityRenamer = entityRenamer;
//        this.renderer = renderer;
//        this.dataFactory = dataFactory;
//        this.applyChanges = applyChanges;
//        this.discussionThreadRepository = discussionThreadRepository;
//    }
//
//    @Nonnull
//    @Override
//    public Class<ChangeEntityIRIAction> getActionClass() {
//        return ChangeEntityIRIAction.class;
//    }
//
//    @Nullable
//    @Override
//    protected BuiltInAction getRequiredExecutableBuiltInAction(ChangeEntityIRIAction action) {
//        return BuiltInAction.EDIT_ONTOLOGY;
//    }
//
//    @Nonnull
//    @Override
//    public ChangeEntityIRIResult execute(@Nonnull ChangeEntityIRIAction action,
//                                         @Nonnull ExecutionContext executionContext) {
//        var entity = action.getEntity();
//        var theNewIri = action.getTheNewIri();
//        var renameMap = ImmutableMap.of(entity, theNewIri);
//        var changeList = entityRenamer.generateChanges(renameMap);
//        var oldRendering = renderer.getRendering(entity);
//        var changeDescription = getChangeDescription(entity, theNewIri);
//        var changeListGenerator = new FixedChangeListGenerator<>(changeList, entity, changeDescription);
//        applyChanges.applyChanges(executionContext.getUserId(), changeListGenerator);
//        var theNewEntity = dataFactory.getOWLEntity(entity.getEntityType(), theNewIri);
//
//        discussionThreadRepository.replaceEntity(projectId, entity, theNewEntity);
//        // TODO: Mentions of the entity need replacing too
//        // TODO: Replacing in match criteria
//        var newRendering = renderer.getRendering(theNewEntity);
//        return new ChangeEntityIRIResult(projectId,
//                                         oldRendering,
//                                         newRendering);
//    }
//
//    private String getChangeDescription(OWLEntity entity, IRI theNewIri) {
//        return String.format("Changed %s IRI from %s to %s",
//                              entity.getEntityType().getPrintName(),
//                              entity.getIRI(),
//                              theNewIri);
//    }
}
