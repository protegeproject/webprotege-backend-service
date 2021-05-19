package edu.stanford.protege.webprotege.revision;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectChangesManager_IT {

//    public static final int CHANGE_COUNT = 100;
//
//    @Rule
//    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
//
//    private File changeHistoryFile;
//
//    private ProjectChangesManager changesManager;
//
//    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());
//
//    @Mock
//    private ProjectDetailsRepository repo;
//
//    @Mock
//    private DefaultOntologyIdManager defaultOntologyIdManager;
//
//    @Mock
//    private ChangeHistoryFileFactory changeHistoryFileFactory;
//
//    @Before
//    public void setUp() throws Exception {
//        changeHistoryFile = temporaryFolder.newFile();
//        when(changeHistoryFileFactory.getChangeHistoryFile(projectId))
//                .thenReturn(changeHistoryFile);
//        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//        OWLOntology rootOntology = manager.createOntology(IRI.create("http://stuff.com/ont"));
//        OWLDataFactory dataFactory = manager.getOWLDataFactory();
//        OntologyChangeRecordTranslator changeRecordTranslator = new OntologyChangeRecordTranslatorImpl();
//        RevisionManager revisionManager = new RevisionManagerImpl(new RevisionStoreImpl(
//                projectId,
//                changeHistoryFileFactory,
//                dataFactory,
//                changeRecordTranslator));
//        when(defaultOntologyIdManager.getDefaultOntologyId())
//                .thenReturn(rootOntology.getOntologyID());
//        when(repo.getDisplayNameLanguages(projectId)).thenReturn(ImmutableList.of());
//
//        var ontologiesIndex = new ProjectOntologiesIndexImpl();
//        ontologiesIndex.init(revisionManager);
//
//        var annotationAssertionsIndex = new AnnotationAssertionAxiomsBySubjectIndexImpl();
//        var projectAnnotationAssertionsIndex = new ProjectAnnotationAssertionAxiomsBySubjectIndexImpl(ontologiesIndex,
//                                                                                                      annotationAssertionsIndex);
//
//        var axiomsByEntityReference = new AxiomsByEntityReferenceIndexImpl(dataFactory);
//        LanguageManager languageManager = new LanguageManager(projectId, new ActiveLanguagesManagerImpl(projectId,
//                                                                                                        axiomsByEntityReference,
//                                                                                                        ontologiesIndex), repo);
//
//        var entitiesInOntologySignatureByIri = new EntitiesInOntologySignatureByIriIndexImpl(axiomsByEntityReference,
//                                                                                             new OntologyAnnotationsIndexImpl());
//        var entitiesInSignatureIndex = new EntitiesInProjectSignatureByIriIndexImpl(ontologiesIndex,
//                                                                                    entitiesInOntologySignatureByIri);
//        var ontologySignatureIndex = new OntologySignatureIndexImpl(axiomsByEntityReference);
//        var projectSignatureIndex = new ProjectSignatureIndexImpl(ontologiesIndex, ontologySignatureIndex);
//
//        var multilingualDictionary = new MultiLingualDictionaryLuceneImpl()
//        DictionaryManager dictionaryManager = new DictionaryManager(languageManager,
//                                                                    multilingualDictionary,
//                                                                    new MultilingualDictionaryUpdater() {
//                                                                        @Override
//                                                                        public void update(@Nonnull Collection<OWLEntity> entities,
//                                                                                           @Nonnull List<DictionaryLanguage> languages) {
//
//                                                                        }
//                                                                    },
//
//                                                                    new BuiltInShortFormDictionary(new ShortFormCache(),
//                                                                                                   dataFactory));
//        ShortFormAdapter shortFormAdapter = new ShortFormAdapter(dictionaryManager);
//        OWLEntityComparator entityComparator = new OWLEntityComparator(
//                shortFormAdapter
//        );
//        OWLClassExpressionSelector classExpressionSelector = new OWLClassExpressionSelector(entityComparator);
//        OWLObjectPropertyExpressionSelector objectPropertyExpressionSelector = new OWLObjectPropertyExpressionSelector(
//                entityComparator);
//        OWLDataPropertyExpressionSelector dataPropertyExpressionSelector = new OWLDataPropertyExpressionSelector(
//                entityComparator);
//        OWLIndividualSelector individualSelector = new OWLIndividualSelector(entityComparator);
//        SWRLAtomSelector atomSelector = new SWRLAtomSelector((o1, o2) -> 0);
//        RenderingManager renderingManager = new RenderingManager(
//                new DictionaryManager(languageManager, multilingualDictionary, multilingualDictionary, new BuiltInShortFormDictionary(new ShortFormCache(), dataFactory)),
//                NullDeprecatedEntityChecker.get(),
//                new ManchesterSyntaxObjectRenderer(
//                        shortFormAdapter,
//                        new EntityIRICheckerImpl(entitiesInSignatureIndex),
//                        LiteralStyle.BRACKETED,
//                        new DefaultHttpLinkRenderer(),
//                        new MarkdownLiteralRenderer()
//                ));
//
//        AxiomComparatorImpl axiomComparator = new AxiomComparatorImpl(
//                new AxiomBySubjectComparator(
//                        new AxiomSubjectProvider(
//                                classExpressionSelector,
//                                objectPropertyExpressionSelector,
//                                dataPropertyExpressionSelector,
//                                individualSelector,
//                                atomSelector
//                        ),
//                        new OWLObjectComparatorImpl(renderingManager)
//                ),
//                new AxiomByTypeComparator(DefaultAxiomTypeOrdering.get()),
//                new AxiomByRenderingComparator(new OWLObjectRendererImpl(renderingManager))
//        );
//        changesManager = new ProjectChangesManager(projectId, revisionManager,
//                                                   renderingManager,
//                                                   new OntologyChangeComparator(
//                        axiomComparator,
//                        (o1, o2) -> 0
//                ),
//                                                   () -> new Revision2DiffElementsTranslator(new WebProtegeOntologyIRIShortFormProvider(defaultOntologyIdManager),
//                                                                                             defaultOntologyIdManager,
//                                                                                             ontologiesIndex));
//
//
//        createChanges(manager, rootOntology, dataFactory, revisionManager);
//    }
//
//    private static void createChanges(OWLOntologyManager manager,
//                       OWLOntology rootOntology,
//                       OWLDataFactory dataFactory,
//                       RevisionManager revisionManager) {
//        PrefixManager pm = new DefaultPrefixManager();
//        pm.setDefaultPrefix("http://stuff.com/" );
//        List<OntologyChange> changeList = new ArrayList<>();
//        for (int i = 0; i < CHANGE_COUNT; i++) {
//            OWLClass clsA = dataFactory.getOWLClass(":A" + i, pm);
//            OWLClass clsB = dataFactory.getOWLClass(":B" + i, pm);
//
//            OWLSubClassOfAxiom ax = dataFactory.getOWLSubClassOfAxiom(clsA, clsB);
//            changeList.add(AddAxiomChange.of(rootOntology.getOntologyID(), ax));
//
//            OWLAnnotationProperty rdfsLabel = dataFactory.getRDFSLabel();
//            IRI annotationSubject = clsA.getIRI();
//            OWLAnnotationAssertionAxiom labAxA = dataFactory.getOWLAnnotationAssertionAxiom(
//                    rdfsLabel,
//                    annotationSubject,
//                    dataFactory.getOWLLiteral("Class A " + i)
//            );
//            changeList.add(AddAxiomChange.of(rootOntology.getOntologyID(), labAxA));
//
//            OWLAnnotationAssertionAxiom labAxB = dataFactory.getOWLAnnotationAssertionAxiom(
//                    rdfsLabel,
//                    annotationSubject,
//                    dataFactory.getOWLLiteral("Class B " + i)
//            );
//            changeList.add(AddAxiomChange.of(rootOntology.getOntologyID(), labAxB));
//        }
//        OntologyChangeTranslator translator = new OntologyChangeTranslator(new OntologyChangeTranslatorVisitor(manager));
//
//        manager.applyChanges(changeList.stream().map(translator::toOwlOntologyChange).collect(toList()));
//        revisionManager.addRevision(UserId.getUserId("MH" ),
//                                    changeList,
//                                    "Adding axioms");
//    }
//
//    @Test
//    public void shouldSaveChangesToFile() {
//        assertThat(changeHistoryFile.length(), is(greaterThan(0L)));
//    }
//
//    @Test
//    public void shouldGetChanges() {
//        Page<ProjectChange> projectChanges = changesManager.getProjectChanges(Optional.empty(),
//                                                                              PageRequest.requestFirstPage());
//        assertThat(projectChanges.getPageElements().size(), is(1));
//    }
//
//    @Test
//    public void shouldContainOntologyChanges() {
//        Page<ProjectChange> projectChanges = changesManager.getProjectChanges(Optional.empty(),
//                                                                                       PageRequest.requestFirstPage());
//        assertThat(projectChanges.getPageElements().get(0).getChangeCount(), is(3 * CHANGE_COUNT));
//    }
}
