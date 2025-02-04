package edu.stanford.protege.webprotege.inject.project;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class RootOntologyDocumentProvider implements Provider<File> {

    private final File projectDirectory;



    @Inject
    public RootOntologyDocumentProvider(@ProjectDirectory File projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public File get() {
        return new File(getOntologyDataDirectory(), "root-ontology.binary");
    }

    private File getOntologyDataDirectory() {
        return new File(projectDirectory, "ontology-data");
    }
}
