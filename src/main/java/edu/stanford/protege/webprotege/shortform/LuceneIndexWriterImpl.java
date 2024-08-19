package edu.stanford.protege.webprotege.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.protege.webprotege.HasDispose;
import edu.stanford.protege.webprotege.index.BuiltInOwlEntitiesIndex;
import edu.stanford.protege.webprotege.index.EntitiesInProjectSignatureIndex;
import edu.stanford.protege.webprotege.index.ProjectSignatureIndex;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.search.EntitySearchFilterIndexesManager;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class LuceneIndexWriterImpl implements LuceneIndexWriter, HasDispose, EntitySearchFilterIndexesManager {

    private static final Logger logger = LoggerFactory.getLogger(LuceneIndexWriterImpl.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Directory luceneDirectory;

    @Nonnull
    private final LuceneEntityDocumentTranslator luceneEntityDocumentTranslator;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Nonnull
    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    @Nonnull
    private final IndexWriter indexWriter;

    @Nonnull
    private final SearcherManager searcherManager;

    @Nonnull
    private final BuiltInOwlEntitiesIndex builtInOwlEntitiesIndex;


    private final ExecutorService executorService;

    private final IndexWriter inMemoryIndexWriter;

    @Inject
    public LuceneIndexWriterImpl(@Nonnull ProjectId projectId,
                                 @Nonnull Directory luceneDirectory,
                                 @Nonnull LuceneEntityDocumentTranslator luceneEntityDocumentTranslator,
                                 @Nonnull ProjectSignatureIndex projectSignatureIndex,
                                 @Nonnull EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex,
                                 @Nonnull IndexWriter indexWriter,
                                 @Nonnull SearcherManager searcherManager,
                                 @Nonnull BuiltInOwlEntitiesIndex builtInOwlEntitiesIndex, ExecutorService executorService, IndexWriter inMemoryIndexWriter) {
        this.projectId = projectId;
        this.luceneDirectory = luceneDirectory;
        this.luceneEntityDocumentTranslator = luceneEntityDocumentTranslator;
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.indexWriter = indexWriter;
        this.searcherManager = searcherManager;
        this.builtInOwlEntitiesIndex = checkNotNull(builtInOwlEntitiesIndex);
        this.executorService = executorService;
        this.inMemoryIndexWriter = inMemoryIndexWriter;
    }

    @Override
    public void updateEntitySearchFilterIndexes() {
        try {
            rebuildIndex();
        } catch (IOException e) {
            logger.error("An error occurred while rebuilding the entity search filter index", e);
        }
    }

    @Override
    public void rebuildIndex() throws IOException {
        inMemoryIndexWriter.deleteAll();
        buildAndWriteIndex(inMemoryIndexWriter);
/*        executorService.submit(() -> {
            try {
                indexWriter.deleteAll();
                buildAndWriteIndex(indexWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });*/
    }

    @Override
    public void writeIndex() throws IOException {

        buildAndWriteIndex(inMemoryIndexWriter);
/*        if(DirectoryReader.indexExists(luceneDirectory)) {
            logger.info("{} Lucene index already exists", projectId);
            return;
        } else {
            executorService.submit(() -> {
                try {
                    buildAndWriteIndex(indexWriter);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }*/
    }

    private void buildAndWriteIndex(IndexWriter indexWriter) throws IOException {
        logger.info("{} Building lucene index", projectId);
        var stopwatch = Stopwatch.createStarted();

        projectSignatureIndex.getSignature()
                             .map(luceneEntityDocumentTranslator::getLuceneDocument)
                             .forEach(document -> this.addDocumentToIndex(document, indexWriter));
       builtInOwlEntitiesIndex.getBuiltInEntities()
                               .filter(entity -> !entitiesInProjectSignatureIndex.containsEntityInSignature(entity))
                               .map(luceneEntityDocumentTranslator::getLuceneDocument)
                                .forEach(document -> this.addDocumentToIndex(document, indexWriter));
        indexWriter.commit();
        searcherManager.maybeRefreshBlocking();
        logger.info("{} Built lucene based dictionary in {} ms", projectId, stopwatch.elapsed().toMillis());
    }

    private int counter = 0;

    public void addDocumentToIndex(Document doc, IndexWriter indexWriter) {
        try {
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PreDestroy
    @Override
    public void dispose() {
        try {
            inMemoryIndexWriter.close();
            //indexWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
