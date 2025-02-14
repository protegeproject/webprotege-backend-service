package edu.stanford.protege.webprotege.frame;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.index.OntologyAnnotationsIndex;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.renderer.ContextRenderer;
import edu.stanford.protege.webprotege.shortform.WebProtegeOntologyIRIShortFormProvider;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesActionHandler extends AbstractProjectActionHandler<GetOntologyFramesAction, GetOntologyFramesResult> {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Nonnull
    private final ContextRenderer renderer;

    @Inject
    public GetOntologyFramesActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                          @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                                          @Nonnull WebProtegeOntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                                          @Nonnull ContextRenderer renderer) {
        super(accessManager);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.ontologyAnnotationsIndex = checkNotNull(ontologyAnnotationsIndex);
        this.ontologyIRIShortFormProvider = checkNotNull(ontologyIRIShortFormProvider);
        this.renderer = checkNotNull(renderer);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOntologyFramesAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOntologyFramesResult execute(@Nonnull GetOntologyFramesAction action, @Nonnull ExecutionContext executionContext) {
        var frames = projectOntologiesIndex.getOntologyIds()
                .map(this::toOntologyFrame)
                .collect(toImmutableList());
        return new GetOntologyFramesResult(frames);
    }

    private OntologyFrame toOntologyFrame(OWLOntologyID ontId) {
        var ontologyAnnotations = ontologyAnnotationsIndex.getOntologyAnnotations(ontId)
                .map(this::toPropertyValue)
                .collect(Collectors.toList());
        var propertyValueList = new PropertyValueList(ontologyAnnotations);
        var shortForm = ontologyIRIShortFormProvider.getShortForm(ontId);
        return new OntologyFrame(ontId, propertyValueList, shortForm);
    }

    private PropertyAnnotationValue toPropertyValue(OWLAnnotation annotation) {
        return PropertyAnnotationValue.get(
                renderer.getAnnotationPropertyData(annotation.getProperty()),
                renderer.getAnnotationValueData(annotation.getValue()),
                State.ASSERTED
        );
    }

    @Nonnull
    @Override
    public Class<GetOntologyFramesAction> getActionClass() {
        return GetOntologyFramesAction.class;
    }
}
