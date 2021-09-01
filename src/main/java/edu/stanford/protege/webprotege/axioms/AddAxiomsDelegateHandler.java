package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.change.AddAxiomChange;
import edu.stanford.protege.webprotege.change.FixedChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.download.DownloadFormat;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import edu.stanford.protege.webprotege.project.chg.ChangeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
public class AddAxiomsDelegateHandler extends AbstractProjectActionHandler<AddAxiomsRequest, AddAxiomsResponse> {

    private final Logger logger = LoggerFactory.getLogger(AddAxiomsDelegateHandler.class);

    private final ChangeManager changeManager;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    public AddAxiomsDelegateHandler(AccessManager accessManager,
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
        var projectId = action.projectId();
        try {
            logger.info("{} Received request to load axioms", projectId);
            var documentSource = new StringDocumentSource(action.ontologyDocument(),
                                                          createDocumentIri(projectId),
                                                          getDocumentFormat(action.mimeType()),
                                                          action.mimeType());

            var loaderConfig = new OWLOntologyLoaderConfiguration()
                    .setReportStackTraces(false)
                    .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT)
                    .setFollowRedirects(true);

            var manager = OWLManager.createOWLOntologyManager();
            var ontology = manager.loadOntologyFromOntologyDocument(documentSource, loaderConfig);

            var ontologyDocument = getTargetOntologyId(ontology);
            var changes = ontology.getAxioms()
                                  .stream()
                                  .map(ax -> new AddAxiomChange(ontologyDocument, ax))
                                  .collect(Collectors.<OntologyChange>toList());
            logger.info("{} Loaded {} axioms", projectId, changes.size());
            var result = changeManager.applyChanges(executionContext.getUserId(),
                                                    new FixedChangeListGenerator<>(changes,
                                                                                   "Added axioms",
                                                                                   action.commitMessage()));
            var axiomsCount = result.getChangeList().size();
            logger.info("{} Successfully applied changes.  This resulted in {} axiom additions.", projectId, axiomsCount);
            return new AddAxiomsResponse(axiomsCount);
        } catch (OWLOntologyCreationException e) {
            logger.info("{} An error occurred when parsing the supplied ontology: {}", projectId, e.getMessage());
            throw new CommandExecutionException(HttpStatus.BAD_REQUEST);
        }
    }

    private OWLDocumentFormat getDocumentFormat(String mimeType) {
        return Arrays.stream(DownloadFormat.values())
                .filter(format -> format.getMimeType().equalsIgnoreCase(mimeType))
                .findFirst()
                .map(DownloadFormat::getDocumentFormat)
                .orElse(DownloadFormat.getDefaultFormat().getDocumentFormat());
    }

    private IRI createDocumentIri(ProjectId projectId) {
        return IRI.create(String.format("http://webprotege.stanford.edu/%s/ontologies/temp", projectId.id()));
    }

    @NotNull
    private OWLOntologyID getTargetOntologyId(OWLOntology ontology) {
        final OWLOntologyID ontologyDocument;
        if (ontology.isAnonymous()) {
            ontologyDocument = defaultOntologyIdManager.getDefaultOntologyId();
        }
        else {
            ontologyDocument = ontology.getOntologyID();
        }
        return ontologyDocument;
    }
}
