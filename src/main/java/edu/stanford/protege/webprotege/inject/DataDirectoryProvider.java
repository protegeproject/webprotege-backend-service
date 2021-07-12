package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.app.WebProtegeProperties;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class DataDirectoryProvider implements Provider<File> {

    @Value("${webprotege.directories.data}")
    private Path dataDirectoryPath;

    @Inject
    public DataDirectoryProvider() {
    }

    @Override
    public File get() {
        return dataDirectoryPath.toFile();
    }
}
