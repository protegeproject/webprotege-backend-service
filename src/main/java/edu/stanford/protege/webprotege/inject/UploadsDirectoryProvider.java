package edu.stanford.protege.webprotege.inject;

import org.springframework.beans.factory.annotation.Value;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.io.File;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public class UploadsDirectoryProvider implements Provider<File> {

    @Value("${webprotege.directories.uploads}")
    private Path uploadsDirectoryPath;

    @Inject
    public UploadsDirectoryProvider() {
    }

    @Override
    public File get() {
        return uploadsDirectoryPath.toFile();
    }
}
