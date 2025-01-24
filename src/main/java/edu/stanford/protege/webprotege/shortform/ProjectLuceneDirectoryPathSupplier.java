package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.nio.file.Path;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class ProjectLuceneDirectoryPathSupplier implements Supplier<Path> {

    @Nonnull
    private final Path luceneIndexesDirectory;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public ProjectLuceneDirectoryPathSupplier(@Nonnull @LuceneIndexesDirectory Path luceneIndexesDirectory,
                                              @Nonnull ProjectId projectId) {
        this.luceneIndexesDirectory = checkNotNull(luceneIndexesDirectory);
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public Path get() {
        return luceneIndexesDirectory.resolve(projectId.id());
    }
}
