package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class GetRenderedOwlEntitiesActionHandler  extends AbstractProjectActionHandler<GetRenderedOwlEntitiesAction, GetRenderedOwlEntitiesResult> {


    private final EntityNodeRenderer renderingManager;

    @Inject
    public GetRenderedOwlEntitiesActionHandler(AccessManager accessManager, EntityNodeRenderer renderingManager) {
        super(accessManager);
        this.renderingManager = renderingManager;
    }

    @NotNull
    @Override
    public Class<GetRenderedOwlEntitiesAction> getActionClass() {
        return GetRenderedOwlEntitiesAction.class;
    }

    @NotNull
    @Override
    public GetRenderedOwlEntitiesResult execute(@NotNull GetRenderedOwlEntitiesAction action, @NotNull ExecutionContext executionContext) {
        return new GetRenderedOwlEntitiesResult(action.entityIris().stream()
                .map(iri -> new OWLClassImpl(IRI.create(iri)))
                .map(owlClass -> renderingManager.render(owlClass))
                .collect(Collectors.toList()));
    }
}
