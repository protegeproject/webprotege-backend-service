package edu.stanford.protege.webprotege.upload;

import edu.stanford.protege.webprotege.csv.DocumentId;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
public interface DocumentResolver {

    /**
     * Resolve the specified {@link DocumentId} to a path on the webprotege.
     */
    Path resolve(@Nonnull DocumentId documentId);
}
