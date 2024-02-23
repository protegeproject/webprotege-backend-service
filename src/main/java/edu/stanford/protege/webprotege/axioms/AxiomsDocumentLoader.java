package edu.stanford.protege.webprotege.axioms;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.download.DownloadFormat;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;

import javax.annotation.Nonnull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-01
 */
public class AxiomsDocumentLoader {

    private static final Logger logger = LoggerFactory.getLogger(AxiomsDocumentLoader.class);

    private final ProjectId projectId;

    private final String ontologyDocument;

    private final String mimeType;

    private final OWLOntologyID defaultOntologyId;

    public AxiomsDocumentLoader(ProjectId projectId, String ontologyDocument, String mimeType, OWLOntologyID defaultOntologyId) {
        this.projectId = projectId;
        this.ontologyDocument = ontologyDocument;
        this.mimeType = mimeType;
        this.defaultOntologyId = defaultOntologyId;
    }

    public <R> List<R> loadAxioms(BiFunction<OWLAxiom, OWLOntologyID, R> function) {
        try {
            logger.info("{} Loading axiomsSource", projectId);
            var documentSource = new StringDocumentSource(ontologyDocument,
                                                          createDocumentIri(),
                                                          getDocumentFormat(),
                                                          mimeType);

            var loaderConfig = new OWLOntologyLoaderConfiguration()
                    .setReportStackTraces(false)
                    .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT)
                    .setFollowRedirects(true);

            var manager = OWLManager.createOWLOntologyManager();
            var ontology = manager.loadOntologyFromOntologyDocument(documentSource, loaderConfig);

            var ontologyDocumentId = getTargetOntologyId(ontology);
            var result = ontology.getAxioms()
                                  .stream()
                                  .map(ax -> function.apply(ax, ontologyDocumentId))
                                  .collect(Collectors.toList());
            logger.info("{} Loaded {} axiomsSource", projectId, result.size());
            return result;
        } catch (OWLOntologyCreationException e) {
            logger.info("{} An error occurred when parsing the supplied ontology document: {}", projectId, e.getMessage());
            throw new CommandExecutionException(HttpStatus.BAD_REQUEST);
        }
    }

    private OWLDocumentFormat getDocumentFormat() {
        return Arrays.stream(DownloadFormat.values())
                     .filter(format -> format.getMimeType().equalsIgnoreCase(mimeType))
                     .findFirst()
                     .map(DownloadFormat::getDocumentFormat)
                     .orElse(DownloadFormat.getDefaultFormat().getDocumentFormat());
    }

    private IRI createDocumentIri() {
        return IRI.create(String.format("http://webprotege.stanford.edu/%s/ontologies/temp", projectId.id()));
    }

    @Nonnull
    private OWLOntologyID getTargetOntologyId(OWLOntology ontology) {
        final OWLOntologyID ontologyDocument;
        if (ontology.isAnonymous()) {
            ontologyDocument = defaultOntologyId;
        }
        else {
            ontologyDocument = ontology.getOntologyID();
        }
        return ontologyDocument;
    }
}
