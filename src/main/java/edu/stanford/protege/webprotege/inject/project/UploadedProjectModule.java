package edu.stanford.protege.webprotege.inject.project;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import edu.stanford.protege.webprotege.app.WebProtegeProperties;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.inject.DataDirectoryProvider;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.inject.WebProtegePropertiesProvider;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.mansyntax.render.*;
import edu.stanford.protege.webprotege.match.EntityMatcherFactory;
import edu.stanford.protege.webprotege.match.Matcher;
import edu.stanford.protege.webprotege.match.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.project.BuiltInPrefixDeclarations;
import edu.stanford.protege.webprotege.project.Ontology;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.renderer.LiteralLexicalFormTransformer;
import edu.stanford.protege.webprotege.renderer.ShortFormAdapter;
import edu.stanford.protege.webprotege.repository.ProjectEntitySearchFiltersManager;
import edu.stanford.protege.webprotege.search.EntitySearchFilter;
import edu.stanford.protege.webprotege.shortform.DictionaryLanguage;
import edu.stanford.protege.webprotege.shortform.LuceneIndexesDirectory;
import edu.stanford.protege.webprotege.util.DisposableObjectManager;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.ShortFormProvider;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
public class UploadedProjectModule {

    private final ProjectId projectId;

    private final ImmutableSet<Ontology> ontologies;

    @Nonnull
    private final LanguageManager languagesManager;

    public UploadedProjectModule(@Nonnull ProjectId projectId,
                                 @Nonnull ImmutableSet<Ontology> ontologies,
                                 @Nonnull LanguageManager languagesManager) {
        this.projectId = projectId;
        this.ontologies = checkNotNull(ontologies);
        this.languagesManager = checkNotNull(languagesManager);
    }

    
    @ProjectSingleton
    ProjectId providesProjectId() {
        return projectId;
    }

    
    @ProjectSingleton
    ImmutableSet<Ontology> provideUploadedOntologies() {
        return ontologies;
    }

    
    @ProjectSingleton
    OWLDataFactoryImpl provideOwlDataFactoryImpl() {
        return new OWLDataFactoryImpl();
    }

    
    @ProjectSingleton
    OWLDataFactory provideDataFactory(OWLDataFactoryImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    OWLEntityProvider provideOwlEntityProvider(OWLDataFactory dataFactory) {
        return dataFactory;
    }

    
    @ProjectSingleton
    ProjectOntologiesIndex providesProjectOntologiesIndex(ImmutableSet<Ontology> uploadedOntologies) {
        return new ProjectOntologiesIndex() {
            @Nonnull
            @Override
            public Stream<OWLOntologyID> getOntologyIds() {
                return uploadedOntologies.stream()
                                         .map(Ontology::getOntologyId);
            }
        };
    }

    
    @ProjectSingleton
    AxiomsByTypeIndex providesAxiomsByTypeIndex(ImmutableSet<Ontology> uploadedOntologies) {
        return new AxiomsByTypeIndex() {
            @Override
            public <T extends OWLAxiom> Stream<T> getAxiomsByType(AxiomType<T> axiomType, OWLOntologyID ontologyId) {
                return uploadedOntologies.stream()
                                         .filter(ont -> ont.getOntologyId()
                                                           .equals(ontologyId))
                                         .flatMap(ont -> ont.getAxioms()
                                                            .stream())
                                         .filter(ax -> ax.getAxiomType()
                                                         .equals(axiomType))
                                         .map(ax -> (T) ax);
            }
        };
    }

    
    @ProjectSingleton
    Multimap<IRI, OWLEntity> providesEntitiesByIRIMultimap(ImmutableSet<Ontology> uploadedOntologies) {
        var iri2EntityMap = HashMultimap.<IRI, OWLEntity>create(1000, 1);
        uploadedOntologies.stream()
                          .flatMap(ont -> ont.getAxioms()
                                             .stream())
                          .flatMap(ax -> ax.getSignature()
                                           .stream())
                          .forEach(entity -> iri2EntityMap.put(entity.getIRI(), entity));
        return iri2EntityMap;
    }

    
    @ProjectSingleton
    EntitiesInProjectSignatureByIriIndex providesEntitiesInProjectSignatureByIriIndex(Multimap<IRI, OWLEntity> iri2EntityMap) {
        return new EntitiesInProjectSignatureByIriIndex() {
            @Nonnull
            @Override
            public Stream<OWLEntity> getEntitiesInSignature(@Nonnull IRI entityIri) {
                return iri2EntityMap.get(entityIri)
                                    .stream();
            }
        };
    }

    
    @ProjectSingleton
    AxiomsByEntityReferenceIndex provideAxiomsByEntityReferenceIndex() {
        return (entity, ontologyId) -> ontologies.stream()
                                         .filter(ontology -> ontology.getOntologyId().equals(ontologyId))
                                         .flatMap(ontology ->  ontology.getAxioms().stream())
                                         .filter(ax -> ax.containsEntityInSignature(entity));
    }

    
    @ProjectSingleton
    ProjectSignatureIndex provideProjectSignatureIndex(Multimap<IRI, OWLEntity> iri2EntityMap) {
        return new ProjectSignatureIndex() {
            @Nonnull
            @Override
            public Stream<OWLEntity> getSignature() {
                return iri2EntityMap.values()
                                    .stream();
            }
        };
    }

    
    @ProjectSingleton
    ProjectAnnotationAssertionAxiomsBySubjectIndex projectAnnotationAssertionAxiomsBySubjectIndex() {
        return new ProjectAnnotationAssertionAxiomsBySubjectIndex() {
            @Nonnull
            @Override
            public Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLAnnotationSubject subject) {
                return ontologies.stream()
                                 .flatMap(ont -> ont.getAxioms().stream())
                                 .filter(axiom -> axiom instanceof OWLAnnotationAssertionAxiom)
                                 .map(axiom -> (OWLAnnotationAssertionAxiom) axiom)
                                 .filter(axiom -> axiom.getSubject().equals(subject));
            }
        };
    }

    
    ShortFormProvider provideShortFormProvider(ShortFormAdapter shortFormAdapter) {
        return shortFormAdapter;
    }

    
    OWLObjectRenderer provideOwlObjectRenderer(ManchesterSyntaxObjectRenderer manchesterSyntaxObjectRenderer) {
        return manchesterSyntaxObjectRenderer;
    }

    
    EntityIRIChecker provideEntityIRIChecker(Multimap<IRI, OWLEntity> iri2EntityMap) {
        return new EntityIRIChecker() {
            @Override
            public boolean isEntityIRI(IRI iri) {
                return iri2EntityMap.containsKey(iri);
            }

            @Override
            public Collection<OWLEntity> getEntitiesWithIRI(IRI iri) {
                return ImmutableList.copyOf(iri2EntityMap.get(iri));
            }
        };
    }

    
    HttpLinkRenderer provideLinkRenderer(NullHttpLinkRenderer impl) {
        return impl;
    }

    
    edu.stanford.protege.webprotege.mansyntax.render.LiteralRenderer provideLiteralRenderer() {
        return (literal, stringBuilder) -> stringBuilder.append(literal);
    }

    
    LiteralLexicalFormTransformer provideLiteralLexicalFormTransformer() {
        return lexicalForm -> lexicalForm;
    }

    
    LanguageManager provideLanguageManager() {
        return languagesManager;
    }

    
    ImmutableList<DictionaryLanguage> providesDictionaryLanguages() {
        return ImmutableList.copyOf(languagesManager.getActiveLanguages());
    }

    
    LiteralStyle provideLiteralStyle() {
        return LiteralStyle.REGULAR;
    }

    
    BuiltInPrefixDeclarations provideBuiltInPrefixDeclarations() {
        return BuiltInPrefixDeclarations.get(ImmutableList.of());
    }

    
    @ProjectSingleton
    public BuiltInOwlEntitiesIndex provideBuiltInOwlEntitiesIndex(@Nonnull BuiltInOwlEntitiesIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public BuiltInSkosEntitiesIndex provideBuiltInSkosEntitiesIndex(@Nonnull BuiltInSkosEntitiesIndexImpl impl) {
        return impl;
    }

    
    @ProjectSingleton
    public EntitiesInProjectSignatureIndex provideEntitiesInProjectSignatureIndex() {
        return new EntitiesInProjectSignatureIndex() {
            @Override
            public boolean containsEntityInSignature(@Nonnull OWLEntity entity) {
                return false;
            }
        };
    }

    
    WebProtegeProperties provideWebProtegeProperties(WebProtegePropertiesProvider protegePropertiesProvider) {
        return protegePropertiesProvider.get();
    }

    
    @LuceneIndexesDirectory
    @ProjectSingleton
    Path provideLuceneIndexesDirectory(DataDirectoryProvider dataDirectoryProvider,
                                       ProjectDisposablesManager projectDisposablesManager) {
        Path path = dataDirectoryProvider.get().toPath().resolve("uploads-lucene-indexes")
                                         // Generate a different path for each upload so that we don't get any clashes
                                         .resolve(UUID.randomUUID().toString());
        projectDisposablesManager.register(() -> {
            try {
                FileUtils.deleteDirectory(path.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return path;
    }

    
    @ProjectSingleton
    AnnotationAssertionAxiomsByValueIndex provideAnnotationAssertionAxiomsByValueIndex() {
        return new AnnotationAssertionAxiomsByValueIndex() {
            @Nonnull
            @Override
            public Stream<OWLAnnotationAssertionAxiom> getAxiomsByValue(@Nonnull OWLAnnotationValue value,
                                                                        @Nonnull OWLOntologyID ontologyId) {
                // Okay to return an empty stream here.  Only needed for stubbing purposes
                return Stream.empty();
            }
        };
    }

    
    @ProjectSingleton
    ProjectDisposablesManager provideProjectDisposablesManager(DisposableObjectManager disposableObjectManager) {
        return new ProjectDisposablesManager(disposableObjectManager);
    }

    
    ProjectEntitySearchFiltersManager provideProjectSearchFiltersRepository() {
        return new ProjectEntitySearchFiltersManager() {
            @Nonnull
            @Override
            public ImmutableList<EntitySearchFilter> getSearchFilters() {
                return ImmutableList.of();
            }

            @Override
            public void setSearchFilters(@Nonnull ImmutableList<EntitySearchFilter> searchFilters) {

            }
        };
    }



    
    EntityMatcherFactory provideEntityMatcherFactory() {
        return new EntityMatcherFactory() {
            @Nonnull
            @Override
            public Matcher<OWLEntity> getEntityMatcher(@Nonnull EntityMatchCriteria criteria) {
                return entity -> false;
            }
        };
    }
}
