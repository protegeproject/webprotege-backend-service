package edu.stanford.protege.webprotege.forms;

import com.google.common.collect.ImmutableMap;
import edu.stanford.protege.webprotege.change.ReverseEngineeredChangeDescriptionGeneratorFactory;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.crud.DeleteEntitiesChangeListGeneratorFactory;
import edu.stanford.protege.webprotege.forms.data.FormData;
import edu.stanford.protege.webprotege.forms.processor.FormDataConverter;
import edu.stanford.protege.webprotege.frame.EmptyEntityFrameFactory;
import edu.stanford.protege.webprotege.frame.FrameChangeGeneratorFactory;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
public class EntityFormChangeListGeneratorFactory {

    @Nonnull
    private final FormDataConverter formDataProcessor;

    @Nonnull
    private final ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final FrameChangeGeneratorFactory frameChangeGeneratorFactory;

    @Nonnull
    private final FormFrameConverter formFrameConverter;

    @Nonnull
    private final EmptyEntityFrameFactory emptyEntityFrameFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final DefaultOntologyIdManager rootOntologyProvider;

    private final DeleteEntitiesChangeListGeneratorFactory deleteEntitiesChangeListGeneratorFactory;

    @Inject
    public EntityFormChangeListGeneratorFactory(@Nonnull FormDataConverter formDataProcessor,
                                                @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory reverseEngineeredChangeDescriptionGeneratorFactory,
                                                @Nonnull MessageFormatter messageFormatter,
                                                @Nonnull FrameChangeGeneratorFactory frameChangeGeneratorFactory,
                                                @Nonnull FormFrameConverter formFrameConverter,
                                                @Nonnull EmptyEntityFrameFactory emptyEntityFrameFactory,
                                                @Nonnull RenderingManager renderingManager,
                                                @Nonnull OWLDataFactory dataFactory,
                                                @Nonnull DefaultOntologyIdManager rootOntologyProvider,
                                                @Nonnull DeleteEntitiesChangeListGeneratorFactory deleteEntitiesChangeListGeneratorFactory) {
        this.formDataProcessor = formDataProcessor;
        this.reverseEngineeredChangeDescriptionGeneratorFactory = reverseEngineeredChangeDescriptionGeneratorFactory;
        this.messageFormatter = messageFormatter;
        this.frameChangeGeneratorFactory = frameChangeGeneratorFactory;
        this.formFrameConverter = formFrameConverter;
        this.emptyEntityFrameFactory = emptyEntityFrameFactory;
        this.renderingManager = renderingManager;
        this.dataFactory = dataFactory;
        this.rootOntologyProvider = rootOntologyProvider;
        this.deleteEntitiesChangeListGeneratorFactory = deleteEntitiesChangeListGeneratorFactory;
    }

    /**
     * Create a change generator to edit the pristine form data to the editedFormsData.
     * @param changeRequestId
     * @param subject The subject of the forms
     * @param pristineFormsData The pristine data.
     * @param editedFormsData The edited data.  Note, the Map may contain null values.
     */
    public EntityFormChangeListGenerator create(@Nonnull ChangeRequestId changeRequestId,
                                                @Nonnull OWLEntity subject,
                                                @Nonnull String commitMessage,
                                                @Nonnull ImmutableMap<FormId, FormData> pristineFormsData,
                                                @Nonnull FormDataByFormId editedFormsData) {
        checkNotNull(subject);
        checkNotNull(editedFormsData);
        checkNotNull(pristineFormsData);
        return new EntityFormChangeListGenerator(changeRequestId, subject,
                                                 commitMessage,
                                                 pristineFormsData,
                                                 editedFormsData,
                                                 formDataProcessor,
                                                 messageFormatter,
                                                 frameChangeGeneratorFactory,
                                                 formFrameConverter,
                                                 emptyEntityFrameFactory,
                                                 dataFactory,
                                                 rootOntologyProvider, deleteEntitiesChangeListGeneratorFactory);
    }

    public EntityFormChangeListGenerator createForAdd(@Nonnull ChangeRequestId changeRequestId,
                                                      @Nonnull OWLEntity subject,
                                                      @Nonnull String commitMessage,
                                                      @Nonnull FormDataByFormId formsData) {
        checkNotNull(subject);
        checkNotNull(formsData);
        var emptyFormData = getEmptyFormData(subject, formsData);
        return create(changeRequestId, subject, commitMessage, emptyFormData, formsData);
    }

    public EntityFormChangeListGenerator createForRemove(@Nonnull ChangeRequestId changeRequestId,
                                                         @Nonnull OWLEntity subject,
                                                         @Nonnull String commitMessage,
                                                         @Nonnull ImmutableMap<FormId, FormData> formsData) {
        checkNotNull(subject);
        checkNotNull(formsData);
        var emptyFormData = getEmptyFormData(subject, new FormDataByFormId(formsData));
        return create(changeRequestId, subject, commitMessage, formsData, new FormDataByFormId(emptyFormData));
    }

    private static ImmutableMap<FormId, FormData> getEmptyFormData(@Nonnull OWLEntity subject,
                                                                   @Nonnull FormDataByFormId formsData) {
        return formsData.getFormIds()
                        .stream()
                        .collect(toImmutableMap((FormId formId) -> formId, (FormId formId) -> FormData.empty(subject, formId)));
    }
}
