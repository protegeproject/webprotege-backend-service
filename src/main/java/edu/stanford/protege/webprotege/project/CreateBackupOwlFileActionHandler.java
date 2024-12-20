package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import org.jetbrains.annotations.NotNull;

public class CreateBackupOwlFileActionHandler extends AbstractProjectActionHandler<CreateBackupOwlFileAction, CreateBackupOwlFileResponse> {

    private final ProjectBackupManager projectBackupManager;

    public CreateBackupOwlFileActionHandler(@NotNull AccessManager accessManager, ProjectBackupManager projectBackupManager) {
        super(accessManager);
        this.projectBackupManager = projectBackupManager;
    }

    @NotNull
    @Override
    public Class<CreateBackupOwlFileAction> getActionClass() {
        return CreateBackupOwlFileAction.class;
    }


    @NotNull
    @Override
    public CreateBackupOwlFileResponse execute(@NotNull CreateBackupOwlFileAction action, @NotNull ExecutionContext executionContext) {
        projectBackupManager.createBackup();
        return new CreateBackupOwlFileResponse();
    }
}
