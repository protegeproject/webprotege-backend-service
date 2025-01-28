package edu.stanford.protege.webprotege.inject.project;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.revision.ChangeHistoryFileFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class ChangeHistoryFileProvider implements Provider<File> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeHistoryFileFactory changeHistoryFileFactory;

    @Inject
    public ChangeHistoryFileProvider(@Nonnull ProjectId projectId,
                                     @Nonnull ChangeHistoryFileFactory changeHistoryFileFactory) {
        this.projectId = checkNotNull(projectId);
        this.changeHistoryFileFactory = checkNotNull(changeHistoryFileFactory);
    }

    @Override
    public File get() {
        return changeHistoryFileFactory.getChangeHistoryFile(projectId);
    }
}
