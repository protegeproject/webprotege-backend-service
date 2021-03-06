package edu.stanford.protege.webprotege.shortform;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface LuceneIndexWriter {

    void rebuildIndex() throws IOException;

    void writeIndex() throws IOException;
}
