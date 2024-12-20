package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.inject.ProjectBackupDirectoryProvider;
import edu.stanford.protege.webprotege.inject.project.ProjectDirectoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class ProjectBackupManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectBackupManager.class);

    private final ProjectDirectoryProvider projectDirectoryProvider;
    private final ProjectBackupDirectoryProvider backupDirectoryProvider;

    public ProjectBackupManager(ProjectDirectoryProvider projectDirectoryProvider, ProjectBackupDirectoryProvider backupDirectoryProvider) {
        this.projectDirectoryProvider = projectDirectoryProvider;
        this.backupDirectoryProvider = backupDirectoryProvider;
    }

    public String createBackup() {
        String currentDate = LocalDate.now().toString();
        Path sourcePath = new File(projectDirectoryProvider.get().getAbsolutePath()+ "/change-data/change-data.binary").toPath();
        Path targetDir = new File(backupDirectoryProvider.get().getAbsolutePath() + "/" + currentDate).toPath();
        Path targetPath = new File(backupDirectoryProvider.get().getAbsolutePath() + "/" + currentDate+ "/change-data.binary").toPath();

        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toAbsolutePath().toString();
        } catch (IOException e) {
            LOGGER.error("Error saving the backup file occurred", e);
            throw new RuntimeException("Error saving the backup file occurred", e);
        }
    }

}
