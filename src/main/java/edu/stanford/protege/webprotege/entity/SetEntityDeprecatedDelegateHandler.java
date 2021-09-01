package edu.stanford.protege.webprotege.entity;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangeList;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
public class SetEntityDeprecatedDelegateHandler extends AbstractProjectActionHandler<SetEntityDeprecatedRequest, SetEntityDeprecatedResponse> {

    private final ChangeManager changeManager;

    private final OWLDataFactory dataFactory;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    public SetEntityDeprecatedDelegateHandler(@NotNull AccessManager accessManager,
                                              ChangeManager changeManager,
                                              OWLDataFactory dataFactory,
                                              DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.changeManager = changeManager;
        this.dataFactory = dataFactory;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @NotNull
    @Override
    public Class<SetEntityDeprecatedRequest> getActionClass() {
        return SetEntityDeprecatedRequest.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetEntityDeprecatedRequest action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @NotNull
    @Override
    public SetEntityDeprecatedResponse execute(@NotNull SetEntityDeprecatedRequest action,
                                               @NotNull ExecutionContext executionContext) {

        var axiom = dataFactory.getDeprecatedOWLAnnotationAssertionAxiom(action.entityIri());
        var changes = List.<OntologyChange>of(new AddAxiomChange(defaultOntologyIdManager.getDefaultOntologyId(),
                                                                 axiom));
        var result = changeManager.applyChanges(executionContext.getUserId(),
                                   new FixedChangeListGenerator<>(changes, "", "Deprecated entity"));
        return new SetEntityDeprecatedResponse();
    }
}
