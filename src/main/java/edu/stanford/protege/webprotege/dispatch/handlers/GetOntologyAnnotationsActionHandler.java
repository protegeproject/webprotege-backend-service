package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.frame.PropertyAnnotationValue;
import edu.stanford.protege.webprotege.frame.PropertyValueComparator;
import edu.stanford.protege.webprotege.frame.State;
import edu.stanford.protege.webprotege.index.OntologyAnnotationsIndex;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ontology.GetOntologyAnnotationsAction;
import edu.stanford.protege.webprotege.ontology.GetOntologyAnnotationsResult;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.renderer.RenderingManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.protege.webprotege.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsActionHandler extends AbstractProjectActionHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyManager;

    @Inject
    public GetOntologyAnnotationsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                                               @Nonnull RenderingManager renderingManager,
                                               @Nonnull PropertyValueComparator propertyValueComparator,
                                               @Nonnull DefaultOntologyIdManager defaultOntologyManager) {
        super(accessManager);
        this.ontologyAnnotationsIndex = ontologyAnnotationsIndex;
        this.renderingManager = renderingManager;
        this.propertyValueComparator = propertyValueComparator;
        this.defaultOntologyManager = defaultOntologyManager;
    }

    @Nonnull
    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetOntologyAnnotationsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetOntologyAnnotationsResult execute(@Nonnull GetOntologyAnnotationsAction action, @Nonnull ExecutionContext executionContext) {
        var ontologyId = action.ontologyId().orElse(defaultOntologyManager.getDefaultOntologyId());
        var annotations = ontologyAnnotationsIndex.getOntologyAnnotations(ontologyId)
                                .map(annotation -> PropertyAnnotationValue.get(
                                        renderingManager.getAnnotationPropertyData(annotation.getProperty()),
                                        renderingManager.getRendering(annotation.getValue()),
                                        State.ASSERTED
                                ))
                                .sorted(propertyValueComparator)
                                .collect(toImmutableList());
        return new GetOntologyAnnotationsResult(ontologyId, annotations);
    }

}
