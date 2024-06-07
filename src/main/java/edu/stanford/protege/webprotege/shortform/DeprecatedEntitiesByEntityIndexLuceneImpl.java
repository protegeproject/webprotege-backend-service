package edu.stanford.protege.webprotege.shortform;

import edu.stanford.protege.webprotege.index.DeprecatedEntitiesByEntityIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-04
 */
@ProjectSingleton
public class DeprecatedEntitiesByEntityIndexLuceneImpl implements DeprecatedEntitiesByEntityIndex {


    private final static Logger LOGGER  = LoggerFactory.getLogger(DeprecatedEntitiesByEntityIndexLuceneImpl.class);
    @Nonnull
    private final SearcherManager searcherManager;

    @Nonnull
    private final LuceneEntityDocumentTranslator luceneEntityDocumentTranslator;

    @Inject
    public DeprecatedEntitiesByEntityIndexLuceneImpl(@Nonnull SearcherManager searcherManager,
                                                     @Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator) {
        this.searcherManager = checkNotNull(searcherManager);
        this.luceneEntityDocumentTranslator = checkNotNull(luceneEntityDocumentTranslator);
    }

    @Override
    public boolean isDeprecated(@Nonnull OWLEntity entity) {
        try {
            return isDeprecatedEntity(entity);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean isDeprecatedEntity(@Nonnull OWLEntity entity) throws IOException {
        long startTime = System.currentTimeMillis();

        var indexSearcher = searcherManager.acquire();
        try {
            var entityQuery = luceneEntityDocumentTranslator.getEntityDocumentQuery(entity);
            var deprecatedQuery = new TermQuery(new Term(EntityDocumentFieldNames.DEPRECATED, EntityDocumentFieldNames.DEPRECATED_TRUE));
            var entityDeprecatedQuery = new BooleanQuery.Builder()
                    .add(entityQuery, BooleanClause.Occur.MUST)
                    .add(deprecatedQuery, BooleanClause.Occur.MUST)
                    .build();
            var totalHits = indexSearcher.search(entityDeprecatedQuery, 1).totalHits;
            long endtime = System.currentTimeMillis();
            LOGGER.info("Operation DeprecatedEntitiesByEntityIndexLuceneImpl.isDeprecatedEntity execution time is : " + (endtime-startTime) +"ms");

            return totalHits.value > 0;
        } finally {
            searcherManager.release(indexSearcher);
        }

    }
}
