package edu.stanford.protege.webprotege.manager;

import edu.stanford.protege.webprotege.owlapi.WebProtegeOWLManager;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class WebProtegeOntologyManagerSaveBinaryDocumentTestCase {

    public File temporaryFolder;

    private File ontologyDocumentFile;

    @BeforeEach
    public void setUp() throws IOException {
        temporaryFolder = Files.newTemporaryFolder();
        ontologyDocumentFile = new File(temporaryFolder, "ontology.ttl");
    }

    @AfterEach
    public void tearDown(){
        ontologyDocumentFile.delete();
        temporaryFolder.delete();
    }


    /**
     * Ensures that an ontology can be saved in the binary ontology format.  This doesn't test the format serialization,
     * it tests that the {@link WebProtegeOWLManager} is configured
     * with the ability to save an ontology in that format.
     */
    @Test
    public void shouldSaveOntologyToBinaryDocumentFile()
            throws OWLOntologyCreationException, OWLOntologyStorageException{
        OWLOntologyManager manager = WebProtegeOWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology();
        BinaryOWLOntologyDocumentFormat format = new BinaryOWLOntologyDocumentFormat();
        manager.saveOntology(ontology, format, IRI.create(ontologyDocumentFile));
    }

}
