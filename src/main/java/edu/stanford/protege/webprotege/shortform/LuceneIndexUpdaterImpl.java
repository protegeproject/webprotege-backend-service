package edu.stanford.protege.webprotege.shortform;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
public class LuceneIndexUpdaterImpl implements LuceneIndexUpdater {

    @Nonnull
    private final IndexWriter indexWriter;

    private final IndexWriter inMemoryIndexWriter;

    @Nonnull
    private final Provider<LuceneEntityDocumentTranslator> documentTranslatorProvider;

    @Nonnull
    private final SearcherManager searcherManager;

    private final ExecutorService executorService;

    @Inject
    public LuceneIndexUpdaterImpl(@Nonnull IndexWriter indexWriter,
                                  IndexWriter inMemoryIndexWriter, @Nonnull Provider<LuceneEntityDocumentTranslator> documentTranslatorProvider,
                                  @Nonnull SearcherManager searcherManager, ExecutorService executorService) {
        this.indexWriter = checkNotNull(indexWriter);
        this.inMemoryIndexWriter = inMemoryIndexWriter;
        this.documentTranslatorProvider = checkNotNull(documentTranslatorProvider);
        this.executorService = executorService;
        this.searcherManager = checkNotNull(searcherManager);
    }

    @Override
    public void updateIndexForEntities(@Nonnull Collection<OWLEntity> entities) {
        try {
            updateGivenIndexForEntities(inMemoryIndexWriter, entities);

            this.executorService.submit(() -> {
                try {
                    updateGivenIndexForEntities(indexWriter, entities);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private void updateGivenIndexForEntities(IndexWriter indexWriter, Collection<OWLEntity> entities) throws IOException {
        var documentTranslator = documentTranslatorProvider.get();
        var deleteQueries = entities.stream()
                .map(documentTranslator::getEntityDocumentQuery)
                .toArray(Query[]::new);

        indexWriter.deleteDocuments(deleteQueries);

        entities.stream()
                .map(documentTranslator::getLuceneDocument)
                .forEach((document) -> {
                    addDocument(document, indexWriter);
                });
        indexWriter.commit();
        searcherManager.maybeRefresh();

    }
    private void addDocument(@Nonnull Document document, IndexWriter indexWriter) {
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
