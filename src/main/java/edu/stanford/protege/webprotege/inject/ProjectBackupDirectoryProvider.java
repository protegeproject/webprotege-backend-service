package edu.stanford.protege.webprotege.inject;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Provider;
import java.io.File;
import java.nio.file.Path;

public class ProjectBackupDirectoryProvider implements Provider<File> {

    @Value("${webprotege.directories.backup}")
    private Path backupDirectory;

    private ProjectId projectId;

    public ProjectBackupDirectoryProvider(ProjectId projectId) {
        this.projectId = projectId;
    }


    @Override
    public File get() {
        return new File(backupDirectory.toFile(), projectId.id());
    }
}
