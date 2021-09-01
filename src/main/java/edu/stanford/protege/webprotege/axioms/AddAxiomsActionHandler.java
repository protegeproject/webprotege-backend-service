package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.index.ProjectOntologiesIndex;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
public class AddAxiomsActionHandler extends AbstractProjectActionHandler<AddAxiomsRequest, AddAxiomsResponse> {

    private final ChangeManager changeManager;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    public AddAxiomsActionHandler(AccessManager accessManager,
                                  ChangeManager changeManager,
                                  DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.changeManager = changeManager;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }


    @NotNull
    @Override
    public Class<AddAxiomsRequest> getActionClass() {
        return AddAxiomsRequest.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(AddAxiomsRequest action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @NotNull
    @Override
    public AddAxiomsResponse execute(@NotNull AddAxiomsRequest action, @NotNull ExecutionContext executionContext) {
        try {
            var manager = OWLManager.createOWLOntologyManager();
            var documentSource = new StringDocumentSource(action.ontologyDocument());
            var ontology = manager.loadOntologyFromOntologyDocument(documentSource);
            final OWLOntologyID ontologyDocument;
            if(ontology.isAnonymous()) {
                ontologyDocument = defaultOntologyIdManager.getDefaultOntologyId();
            }
            else {
                ontologyDocument = ontology.getOntologyID();
            }
            var changes = ontology.getAxioms()
                    .stream()
                    .map(ax -> new AddAxiomChange(ontologyDocument, ax))
                    .collect(Collectors.<OntologyChange>toList());
            var result = changeManager.applyChanges(executionContext.getUserId(),
                                       new FixedChangeListGenerator<>(changes, "Added axioms", action.commitMessage()));
            return new AddAxiomsResponse(result.getChangeList().size());
        } catch (OWLOntologyCreationException e) {
            throw new CommandExecutionException(HttpStatus.BAD_REQUEST);
        }
    }
}
