package edu.stanford.protege.webprotege;

import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.DataDirectoryProvider;
import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.project.BuiltInPrefixDeclarations;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.protege.webprotege.search.EntitySearchFilterIndexesManager;
import edu.stanford.protege.webprotege.search.EntitySearchFilterRepository;
import edu.stanford.protege.webprotege.search.ProjectEntitySearchFiltersManagerImpl;
import edu.stanford.protege.webprotege.shortform.*;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.inject.Provider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class LuceneBeansConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LuceneModule.class);

    public static final int MIN_GRAM_SIZE = 2;

    public static final int MAX_GRAM_SIZE = 11;

    @Bean
    FieldNameTranslatorImpl fieldNameTranslatorImpl() {
        return new FieldNameTranslatorImpl();
    }

    @Bean
    @Scope("prototype")
    LuceneEntityDocumentTranslatorImpl luceneEntityDocumentTranslatorImpl(FieldNameTranslator p1,
                                                                          EntityBuiltInStatusDocumentAugmenter p2,
                                                                          EntityLocalNameDocumentAugmenter p3,
                                                                          EntityPrefixedNameDocumentAugmenter p4,
                                                                          EntityOboIdDocumentAugmenter p5,
                                                                          EntityAnnotationAssertionsDocumentAugmenter p6,
                                                                          Provider<SearchFiltersDocumentAugmenter> p7,
                                                                          OWLDataFactory p8) {
        return new LuceneEntityDocumentTranslatorImpl(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Bean
    QueryAnalyzerFactory queryAnalyzerFactory() {
        return new QueryAnalyzerFactory();
    }

    @Bean
    LuceneQueryFactory luceneQueryFactory(FieldNameTranslator p1, QueryAnalyzerFactory p2) {
        return new LuceneQueryFactory(p1, p2);
    }

    @Bean
    LuceneSearchStringTokenizer luceneSearchStringTokenizer(QueryAnalyzerFactory p1) {
        return new LuceneSearchStringTokenizer(p1);
    }

    @Bean
    IndexingAnalyzerWrapper indexingAnalyzerWrapper() {
        return new IndexingAnalyzerWrapper(minGramSize(), maxGramSize());
    }

    @Bean
    IndexingAnalyzerFactory indexingAnalyzerFactory(Provider<IndexingAnalyzerWrapper> p1) {
        return new IndexingAnalyzerFactory(p1);
    }

    @Bean
    LuceneDictionaryLanguageValuesMatcher luceneDictionaryLanguageValuesMatcher(LuceneSearchStringTokenizer p1,
                                                                                FieldNameTranslator p2,
                                                                                IndexingAnalyzerFactory p3) {
        return new LuceneDictionaryLanguageValuesMatcher(p1, p2, p3);
    }

    @Bean
    LuceneIndexImpl luceneIndexImpl(LuceneEntityDocumentTranslator p1,
                                    SearcherManager p2,
                                    LuceneQueryFactory p3,
                                    LuceneDictionaryLanguageValuesMatcher p4,
                                    QueryAnalyzerFactory p5,
                                    MatcherFactory p6) {
        return new LuceneIndexImpl(p1, p2, p3, p4, p5, p6);
    }

    @Bean
    LuceneIndexWriterImpl luceneIndexWriterImpl(ProjectId p1,
                                                Directory p2,
                                                LuceneEntityDocumentTranslator p3,
                                                ProjectSignatureIndex p4,
                                                EntitiesInProjectSignatureIndex p5,
                                                IndexWriter p6,
                                                SearcherManager p7,
                                                BuiltInOwlEntitiesIndex p8) {
        try {
            var impl = new LuceneIndexWriterImpl(p1, p2, p3, p4, p5, p6, p7, p8);
            impl.writeIndex();
            return impl;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @Bean
    MultiLingualShortFormDictionaryLuceneImpl multiLingualShortFormDictionaryLuceneImpl(LuceneIndex p1) {
        return new MultiLingualShortFormDictionaryLuceneImpl(p1);
    }


    @Bean
    SearchableMultiLingualShortFormDictionaryLuceneImpl searchableMultiLingualShortFormDictionaryLuceneImpl(LuceneIndex p1) {
        return new SearchableMultiLingualShortFormDictionaryLuceneImpl(p1);
    }


    @Bean
    MultiLingualDictionaryLuceneImpl multiLingualDictionaryLuceneImpl(MultiLingualShortFormDictionaryLuceneImpl p1,
                                                                      SearchableMultiLingualShortFormDictionaryLuceneImpl p2,
                                                                      MultiLingualShortFormIndexLuceneImpl p3) {
        return new MultiLingualDictionaryLuceneImpl(p1, p2, p3);
    }

    @Bean
    @LuceneIndexesDirectory
    Path luceneIndexesDirectory(DataDirectoryProvider dataDirectoryProvider) {
        var dataDirectory = dataDirectoryProvider.get().toPath();
        return dataDirectory.resolve("lucene-indexes");
    }

    @Bean
    ProjectLuceneDirectoryPathSupplier projectLuceneDirectoryPathSupplier(@LuceneIndexesDirectory Path p1,
                                                                          ProjectId p2) {
        return new ProjectLuceneDirectoryPathSupplier(p1, p2);
    }


    @Bean
    Directory directory(ProjectLuceneDirectoryPathSupplier pathSupplier) {
        try {
            // FSDirectory.open chooses the best implementation for the platform
            return FSDirectory.open(pathSupplier.get());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Bean
    IndexWriterConfig indexingWriterConfig(IndexingAnalyzerFactory indexingAnalyzerFactory) {
        var analyzer = indexingAnalyzerFactory.get();
        var config = new IndexWriterConfig(analyzer);
        return config.setSimilarity(new EntityBasedSimilarity());
    }

    @Bean
    @MinGramSize
    int minGramSize() {
        return MIN_GRAM_SIZE;
    }

    @Bean
    @MaxGramSize
    int maxGramSize() {
        return MAX_GRAM_SIZE;
    }



    @Bean
    ProjectDisposablesManager projectDisposablesManager(DisposableObjectManager disposableObjectManager) {
        return new ProjectDisposablesManager(disposableObjectManager);
    }

    @Bean
    IndexWriter indexWriter(Directory directory,
                            IndexWriterConfig indexWriterConfig,
                            ProjectDisposablesManager projectDisposablesManager,
                            ProjectId projectId) {
        try {
            var indexWriter = new IndexWriter(directory, indexWriterConfig);
            projectDisposablesManager.register(() -> {
                try {
                    indexWriter.close();
                    logger.info("{} Closed lucene index writer", projectId);
                } catch (IOException e) {
                    logger.error("Error when disposing of Project Lucene IndexWriter", e);
                }
            });

            return indexWriter;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    @Bean
    SearcherManager searcherManager(IndexWriter indexWriter,
                                    SearcherFactory searcherFactory,
                                    DisposableObjectManager disposableObjectManager) {
        try {
            var searchManager = new SearcherManager(indexWriter, searcherFactory);
            disposableObjectManager.register(() -> {
                try {
                    searchManager.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return searchManager;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Bean
    SearcherFactory searcherFactory() {
        return new SearcherFactory();
    }

    @Bean
    LuceneIndexUpdaterImpl luceneIndexUpdaterImpl(IndexWriter p1,
                                                  Provider<LuceneEntityDocumentTranslator> p2,
                                                  SearcherManager p3) {
        return new LuceneIndexUpdaterImpl(p1, p2, p3);
    }

    @Bean
    LuceneMultiLingualDictionaryUpdater luceneMultiLingualDictionaryUpdater(LuceneIndexUpdater p1) {
        return new LuceneMultiLingualDictionaryUpdater(p1);
    }

    @Bean
    MultiLingualShortFormIndexLuceneImpl multiLingualShortFormIndexLuceneImpl(LuceneIndex p1) {
        return new MultiLingualShortFormIndexLuceneImpl(p1);
    }

    @Bean
    @Scope("prototype")
    EntitySearchFilterMatchersFactory entitySearchFilterMatchersFactory(ProjectEntitySearchFiltersManager p1,
                                                                        EntityMatcherFactory p2) {
        return new EntitySearchFilterMatchersFactory(p1, p2);
    }

    @Bean
    @Scope("prototype")
    EntityBuiltInStatusDocumentAugmenter entityBuiltInStatusDocumentAugmenter() {
        return new EntityBuiltInStatusDocumentAugmenter();
    }

    @Bean
    @Scope("prototype")
    DictionaryLanguageFieldWriter dictionaryLanguageFieldWriter(FieldNameTranslator p1) {
        return new DictionaryLanguageFieldWriter(p1);
    }

    @Bean
    @Scope("prototype")
    EntityAnnotationAssertionsDocumentAugmenter entityAnnotationAssertionsDocumentAugmenter(
            ProjectAnnotationAssertionAxiomsBySubjectIndex p1,
            DictionaryLanguageFieldWriter p2) {
        return new EntityAnnotationAssertionsDocumentAugmenter(p1, p2);
    }

    @Bean
    @Scope("prototype")
    LocalNameExtractor localNameExtractor() {
        return new LocalNameExtractor();
    }

    @Bean
    @Scope("prototype")
    EntityLocalNameDocumentAugmenter entityLocalNameDocumentAugmenter(LocalNameExtractor p1,
                                                                      DictionaryLanguageFieldWriter p2) {
        return new EntityLocalNameDocumentAugmenter(p1, p2);
    }

    @Bean
    @Scope("prototype")
    EntityOboIdDocumentAugmenter entityOboIdDocumentAugmenter(DictionaryLanguageFieldWriter p1) {
        return new EntityOboIdDocumentAugmenter();
    }

    @Bean
    @Scope("prototype")
    EntityPrefixedNameDocumentAugmenter entityPrefixedNameDocumentAugmenter(DictionaryLanguageFieldWriter p1,
                                                                            BuiltInPrefixDeclarations p2) {
        return new EntityPrefixedNameDocumentAugmenter(p1, p2);
    }

    @Bean
    @Scope("prototype")
    SearchFiltersDocumentAugmenter searchFiltersDocumentAugmenter(EntitySearchFilterMatchersFactory p1) {
        return new SearchFiltersDocumentAugmenter(p1);
    }

    @Bean
    @Scope("prototype")
    ProjectEntitySearchFiltersManagerImpl projectEntitySearchFiltersManager(ProjectId p1,
                                                                            EntitySearchFilterRepository p2,
                                                                            Provider<EntitySearchFilterIndexesManager> p3) {
        return new ProjectEntitySearchFiltersManagerImpl(p1, p2, p3);
    }

    @Bean
    DeprecatedEntitiesIndex deprecatedEntitiesIndex(SearcherManager p1, LuceneEntityDocumentTranslator p2) {
        return new DeprecatedEntitiesIndexLuceneImpl(p1, p2);
    }

}
