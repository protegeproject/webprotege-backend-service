package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.*;
import edu.stanford.protege.webprotege.change.*;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.crud.DeleteEntitiesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.entity.OWLEntityData;
import edu.stanford.protege.webprotege.forms.data.*;
import edu.stanford.protege.webprotege.forms.processor.FormDataConverter;
import edu.stanford.protege.webprotege.frame.*;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import jakarta.inject.Inject;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final ChangeRequestId changeRequestId;

    @Nonnull
    private final FormDataConverter formDataProcessor;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final String commitMessage;

    @Nonnull
    private final ImmutableMap<FormId, FormData> pristineFormsData;

    @Nonnull
    private final FormDataByFormId editedFormsData;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    @Nonnull
    private final FormFrameConverter formFrameConverter;

    @Nonnull
    private final EmptyEntityFrameFactory emptyEntityFrameFactory;

    @Nonnull
    private final OWLEntity subject;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final DeleteEntitiesChangeListGeneratorFactory deleteEntitiesChangeListGeneratorFactory;

    @Nonnull
    private final RenderingManager renderingManager;


    @Inject
    public EntityFormChangeListGenerator(@Nonnull ChangeRequestId changeRequestId,
                                         @Nonnull OWLEntity subject,
                                         @Nonnull String commitMessage,
                                         @Nonnull ImmutableMap<FormId, FormData> pristineFormsData,
                                         @Nonnull FormDataByFormId editedFormData,
                                         @Nonnull FormDataConverter formDataProcessor,
                                         @Nonnull MessageFormatter messageFormatter,
                                         @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                         @Nonnull FormFrameConverter formFrameConverter,
                                         @Nonnull EmptyEntityFrameFactory emptyEntityFrameFactory,
                                         @Nonnull OWLDataFactory dataFactory,
                                         @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                         @Nonnull DeleteEntitiesChangeListGeneratorFactory deleteEntitiesChangeListGeneratorFactory,
                                         @Nonnull RenderingManager renderingManager) {
        this.subject = checkNotNull(subject);
        this.commitMessage = checkNotNull(commitMessage);
        this.pristineFormsData = checkNotNull(pristineFormsData);
        this.editedFormsData = checkNotNull(editedFormData);
        this.formDataProcessor = checkNotNull(formDataProcessor);
        this.messageFormatter = checkNotNull(messageFormatter);
        this.frameChangeGeneratorFactory = checkNotNull(frameChangeGeneratorFactory);
        this.formFrameConverter = checkNotNull(formFrameConverter);
        this.emptyEntityFrameFactory = emptyEntityFrameFactory;
        this.dataFactory = dataFactory;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.deleteEntitiesChangeListGeneratorFactory = deleteEntitiesChangeListGeneratorFactory;
        this.changeRequestId = changeRequestId;
        this.renderingManager = renderingManager;
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        var allChanges = new ArrayList<OntologyChangeList<OWLEntity>>();
        for (FormId formId : pristineFormsData.keySet()) {
            var pristineFormData = pristineFormsData.get(formId);
            var editedFormData = editedFormsData.getFormData(formId);
            if (pristineFormData == null) {
                throw new RuntimeException("Pristine form data not found for form " + formId);
            }
            var editedFormFrame = getFormFrame(editedFormData);
            var pristineFormFrame = formDataProcessor.convert(pristineFormData);
            if (!pristineFormFrame.equals(editedFormFrame)) {
                var pristineFramesBySubject = getFormFrameClosureBySubject(pristineFormFrame);
                var editedFramesBySubject = getFormFrameClosureBySubject(editedFormFrame);
                var changes = generateChangesForFormFrames(pristineFramesBySubject, editedFramesBySubject, context);
                allChanges.addAll(changes);
            }
        }

        if (allChanges.isEmpty()) {
            return emptyChangeList();
        } else {
            return combineIndividualChangeLists(allChanges);
        }
    }

    private FormFrame getFormFrame(@Nonnull Optional<FormData> editedFormData) {
        return editedFormData.map(formDataProcessor::convert)
                .orElse(emptyFormFrame(subject));
    }

    private static FormFrame emptyFormFrame(OWLEntity subject) {
        return FormFrame.get(FormEntitySubject.get(subject));
    }

    private OntologyChangeList<OWLEntity> emptyChangeList() {
        return OntologyChangeList.<OWLEntity>builder().build(subject);
    }

    /**
     * Combines a list of change lists
     *
     * @param changes the list of changes lists
     * @return the combined list with the subject equal to the subject of the first change in the list
     */
    @Nonnull
    public OntologyChangeList<OWLEntity> combineIndividualChangeLists(List<OntologyChangeList<OWLEntity>> changes) {
        var firstChangeList = changes.get(0);
        var combinedChanges = changes.stream()
                .map(OntologyChangeList::getChanges)
                .flatMap(List::stream)
                .collect(toImmutableList());

        return OntologyChangeList.<OWLEntity>builder()/**/.addAll(combinedChanges).build(firstChangeList.getResult());
    }

    private static ImmutableMap<FormEntitySubject, FormFrame> getFormFrameClosureBySubject(FormFrame formFrame) {

        var flattener = new FormFrameFlattener();
        var flattenedFormFrames = flattener.flattenAndMerge(formFrame);
        return flattenedFormFrames.stream()
                .collect(toImmutableMap(FormFrame::getSubject,
                        f -> f));
    }

    private List<OntologyChangeList<OWLEntity>> generateChangesForFormFrames(ImmutableMap<FormEntitySubject, FormFrame> pristineFramesBySubject,
                                                                             ImmutableMap<FormEntitySubject, FormFrame> editedFramesBySubject,
                                                                             ChangeGenerationContext context) {

        var resultBuilder = ImmutableList.<OntologyChangeList<OWLEntity>>builder();
        for (FormEntitySubject subject : pristineFramesBySubject.keySet()) {
            var pristineFrame = pristineFramesBySubject.get(subject);
            var editedFrame = editedFramesBySubject.get(subject);

            var pristineEntityFrame = formFrameConverter.toEntityFrame(pristineFrame).orElseThrow();

            if (editedFrame == null) {
                // Deleted
                var emptyEditedFrame = emptyEntityFrameFactory.getEmptyEntityFrame(subject.getEntity());
                var changes = generateChangeListForFrames(pristineEntityFrame, emptyEditedFrame, context);
                resultBuilder.add(changes);
                var emptyFormFrame = FormFrame.get(FormSubject.get(subject.getEntity()));
                generateChangesForInstances(subject.getEntity(), pristineFrame, emptyFormFrame, resultBuilder);
                if (!this.subject.equals(subject.getEntity())) {
                    // Non-top-level subject.  This needs deleting because it corresponds to a grid row subject,
                    // or sub-form subject
                    var deletionChangeListGenerator = deleteEntitiesChangeListGeneratorFactory.create(Collections.singleton(subject.getEntity()),
                            changeRequestId);
                    var deletionChanges = OntologyChangeList.<OWLEntity>builder()
                            .addAll(deletionChangeListGenerator.generateChanges(context).getChanges())
                            .build(subject.getEntity());
                    resultBuilder.add(deletionChanges);
                }
            } else {
                // Edited, possibly
                var editedEntityFrame = formFrameConverter.toEntityFrame(editedFrame).orElseThrow();
                var changes = generateChangeListForFrames(pristineEntityFrame, editedEntityFrame, context);
                resultBuilder.add(changes);
                // Compute diff of class assertions
                generateChangesForInstances(subject.getEntity(), pristineFrame, editedFrame, resultBuilder);
            }
        }

        for (FormEntitySubject subject : editedFramesBySubject.keySet()) {
            var pristineFrame = pristineFramesBySubject.get(subject);
            if (pristineFrame == null) {
                // Added
                var emptyPristineFrame = emptyEntityFrameFactory.getEmptyEntityFrame(subject.getEntity());
                var addedFormFrame = editedFramesBySubject.get(subject);
                var addedEntityFrame = formFrameConverter.toEntityFrame(addedFormFrame).orElseThrow();
                var changes = generateChangeListForFrames(emptyPristineFrame, addedEntityFrame, context);
                resultBuilder.add(changes);
                // Add all class assertions for instances
                generateChangesForInstances(subject.getEntity(),
                        FormFrame.get(FormSubject.get(subject.getEntity())),
                        addedFormFrame,
                        resultBuilder);
            }
        }
        return resultBuilder.build();
    }


    private OntologyChangeList<OWLEntity> generateChangeListForFrames(PlainEntityFrame pristineFrame,
                                                                      PlainEntityFrame editedFrame,
                                                                      ChangeGenerationContext context) {
        var frameUpdate = FrameUpdate.get(pristineFrame, editedFrame);
        var changeGeneratorFactory = frameChangeGeneratorFactory.create(changeRequestId, frameUpdate);

        return changeGeneratorFactory.generateChanges(context);
    }

    private void generateChangesForInstances(OWLEntity subject,
                                             FormFrame pristineFrame,
                                             FormFrame editedFrame,
                                             ImmutableList.Builder<OntologyChangeList<OWLEntity>> changeListBuilder) {
        if (!(subject instanceof OWLClass)) {
            return;
        }
        var ontologyChangeList = OntologyChangeList.<OWLEntity>builder();
        var subjectCls = (OWLClass) subject;
        var pristineInstances = pristineFrame.getInstances();
        var editedInstances = editedFrame.getInstances();
        for (var pristineInstance : pristineInstances) {
            if (!editedInstances.contains(pristineInstance)) {
                // Deleted
                var axiom = dataFactory.getOWLClassAssertionAxiom(subjectCls, pristineInstance);
                // TODO: Project ontologies?
                ontologyChangeList.removeAxiom(defaultOntologyIdManager.getDefaultOntologyId(), axiom);
            }
        }
        for (var editedInstance : editedInstances) {
            if (!pristineInstances.contains(editedInstance)) {
                // Added
                var axiom = dataFactory.getOWLClassAssertionAxiom(subjectCls, editedInstance);
                ontologyChangeList.addAxiom(defaultOntologyIdManager.getDefaultOntologyId(), axiom);
            }
        }
        changeListBuilder.add(ontologyChangeList.build(subject));

    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        OWLEntity entity = result.getSubject();
        OWLEntityData renderedEntity = renderingManager.getRendering(entity);
        StringBuilder sb = new StringBuilder();
        sb.append(messageFormatter.format("Edited {0}", renderedEntity.getBrowserText()));
        if (!commitMessage.isBlank()) {
            sb.append(" : ");
            sb.append(commitMessage.trim());
        }

        return sb.toString();
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}
