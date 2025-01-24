package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.ObjectProvider;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
@ProjectSingleton
public class IndexingAnalyzerFactory {

    private final Provider<IndexingAnalyzerWrapper> indexingAnalyzerWrapperProvider;

    @Inject
    public IndexingAnalyzerFactory(Provider<IndexingAnalyzerWrapper> indexingAnalyzerWrapperProvider) {
        this.indexingAnalyzerWrapperProvider = checkNotNull(indexingAnalyzerWrapperProvider);
    }

    @Nonnull
    public Analyzer get() {
        return indexingAnalyzerWrapperProvider.get();
    }
}
