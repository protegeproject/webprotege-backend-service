package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.csv.DocumentId;
import edu.stanford.protege.webprotege.ipc.CommandExecutor;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesRequest;
import edu.stanford.protege.webprotege.ontology.ProcessUploadedOntologiesResponse;
import edu.stanford.protege.webprotege.revision.RevisionManagerFactory;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentHandler;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentPreamble;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.change.OntologyChangeDataList;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParser;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
public class ProjectImporter {

    private final CommandExecutor<ProcessUploadedOntologiesRequest, ProcessUploadedOntologiesResponse> processOntologiesExecutor;


    public ProjectImporter(CommandExecutor<ProcessUploadedOntologiesRequest, ProcessUploadedOntologiesResponse> processOntologiesExecutor) {
        this.processOntologiesExecutor = processOntologiesExecutor;
    }

    public void createProjectFromSources(ProjectId projectId,
                                         DocumentId documentId,
                                         UserId userId) {
        var request = new ProcessUploadedOntologiesRequest(documentId);
        var response = processOntologiesExecutor.execute(request, new ExecutionContext(userId, ""));
        try {
            response.get().ontologies().forEach(o -> System.out.println("Processed ontology at " + o));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //        // Generate change history
//        var revisionManager = revisionManagerFactory.createRevisionManager(projectId);
//        var r = response.get();
//        var ontologies = r.ontologies();
//
//        // Download from MinIO
//        // Parse each ontology
//        // Generate ontology changes for each
//        // Store revision

    }
}
