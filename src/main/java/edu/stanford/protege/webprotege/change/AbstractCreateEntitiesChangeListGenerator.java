package edu.stanford.protege.webprotege.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.entity.EntityShortFormsParser;
import edu.stanford.protege.webprotege.entity.FreshEntityIri;
import edu.stanford.protege.webprotege.icd.IcdConstants;
import edu.stanford.protege.webprotege.msg.MessageFormatter;
import edu.stanford.protege.webprotege.owlapi.RenameMap;
import edu.stanford.protege.webprotege.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.Namespaces;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 * <p>
 *     A base class for generating changes associated with entity creation.  This class accepts a set of string representing
 *     the browser text of the entities to be created, and an (optional) "parent" entity.  The notion of a "parent" entity is dependent
 *     on the type of entities being created.  For example, if classes are being created then the parent entity would
 *     usually be a super class that the freshly created entities will be subclasses of.  If individuals are being created
 *     then the parent entity might be a class that the freshly created individuals are instances of.  Subclasses of this
 *     class defined the exact type of behaviour of the parent entity and the necessary changes that are needed to form
 *     associations with the parent entity in the ontology.
 * </p>
 */
public abstract class AbstractCreateEntitiesChangeListGenerator<E extends OWLEntity, P extends OWLEntity> implements ChangeListGenerator<Set<E>> {

    @Nonnull
    private final EntityType<E> entityType;

    @Nonnull
    private final String sourceText;

    @Nonnull
    private final String langTag;

    @Nonnull
    private final ImmutableSet<P> parents;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final MessageFormatter msg;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final ChangeRequestId changeRequestId;

    private static final Map<String, String> builtInPrefixes = new HashMap<>();

    static {
        builtInPrefixes.put("owl:", Namespaces.OWL.toString());
        builtInPrefixes.put("rdf:", Namespaces.RDF.toString());
        builtInPrefixes.put("rdfs:", Namespaces.RDFS.toString());
        builtInPrefixes.put("skos:", Namespaces.SKOS.toString());
        builtInPrefixes.put("swrl:", Namespaces.SWRL.toString());
        builtInPrefixes.put("swrlb:", Namespaces.SWRLB.toString());
        builtInPrefixes.put("xsd:", Namespaces.XSD.toString());
        builtInPrefixes.put("xml:", Namespaces.XML.toString());
    }

    /**
     * Constructs an {@link AbstractCreateEntitiesChangeListGenerator}.
     * @param entityType The type of entities that are created by this change generator.  Not {@code null}.
     * @param sourceText The set of browser text strings that correspond to short names of the fresh entities that will
     * be created.  Not {@code null}.  May be empty.
     * @param parents The parent entities.  Not {@code null}.
     * @param changeRequestId
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public AbstractCreateEntitiesChangeListGenerator(@Nonnull EntityType<E> entityType,
                                                     @Nonnull String sourceText,
                                                     @Nonnull String langTag,
                                                     @Nonnull ImmutableSet<P> parents,
                                                     @Nonnull OWLDataFactory dataFactory,
                                                     @Nonnull MessageFormatter msg,
                                                     @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                     @Nonnull ChangeRequestId changeRequestId) {
        this.entityType = entityType;
        this.sourceText = sourceText;
        this.langTag = langTag;
        this.parents = parents;
        this.dataFactory = dataFactory;
        this.msg = msg;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.changeRequestId = changeRequestId;
    }

    @Override
    public OntologyChangeList<Set<E>> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Set<E>> builder = new OntologyChangeList.Builder<>();
        Set<E> freshEntities = new HashSet<>();
        EntityShortFormsParser parser = new EntityShortFormsParser();
        List<String> shortForms = parser.parseShortForms(sourceText);
        for (String bt : shortForms) {
            String browserText = bt.trim();
            Optional<String> builtInPrefix = getBuiltInPrefix(browserText);
            E freshEntity;
            if(builtInPrefix.isPresent()) {
                String expanded = expandBuiltInPrefix(browserText);
                freshEntity = DataFactory.getOWLEntity(entityType, expanded);
            }
            else {
                var freshEntityIri = FreshEntityIri.get(browserText,
                                   langTag.trim(),
                                   "",
                                   parents.stream().map(OWLEntity::getIRI).collect(toImmutableSet()));
                freshEntity = DataFactory.getOWLEntity(entityType, freshEntityIri.getIri());
                var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
                builder.add(AddAxiomChange.of(ontologyId, dataFactory.getOWLDeclarationAxiom(freshEntity)));
            }
            for(OWLAxiom axiom : createParentPlacementAxioms(freshEntity, context, parents)) {
                var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
                builder.add(AddAxiomChange.of(ontologyId, axiom));
            }

            /*
            ToDo:
                see if we can make this prettier
             */
            if(entityType.equals(CLASS)){
                IRI propertyIRILabel = IRI.create(IcdConstants.LABEL_PROP);
                IRI propertyIRITitle = IRI.create(IcdConstants.TITLE_PROP);
                IRI iriLangTerm = IRI.create(IcdConstants.LANGUAGE_TERM_CLS);
                var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
                OWLClass langTermClass = dataFactory.getOWLClass(iriLangTerm);

                OWLAnnotationProperty labelProperty = dataFactory.getOWLAnnotationProperty(propertyIRILabel);
                OWLAnnotationProperty titleProperty = dataFactory.getOWLAnnotationProperty(propertyIRITitle);

                OWLLiteral literal = dataFactory.getOWLLiteral(sourceText, langTag);
                OWLNamedIndividual titleIndividual = dataFactory.getOWLNamedIndividual(IRI.create(IcdConstants.NS, "TitleTerm_"+UUID.randomUUID()));

                OWLAnnotationAssertionAxiom titleAxiom = dataFactory.getOWLAnnotationAssertionAxiom(titleProperty, freshEntity.getIRI(), titleIndividual.getIRI());
                OWLAnnotationAssertionAxiom newLabelAxiom = dataFactory.getOWLAnnotationAssertionAxiom(labelProperty, titleIndividual.getIRI(), literal);

                OWLClassAssertionAxiom langTermAxiom = dataFactory.getOWLClassAssertionAxiom(langTermClass, titleIndividual);

                builder.add(AddAxiomChange.of(ontologyId,langTermAxiom));
                builder.add(AddAxiomChange.of(ontologyId,titleAxiom));
                builder.add(AddAxiomChange.of(ontologyId,newLabelAxiom));
            }


            freshEntities.add(freshEntity);
        }
        return builder.build(freshEntities);
    }


    private Optional<String> getBuiltInPrefix(String browserText) {
        for(String prefixName : builtInPrefixes.keySet()) {
            if(browserText.startsWith(prefixName)) {
                return Optional.of(prefixName);
            }
        }
        return Optional.empty();
    }

    private String expandBuiltInPrefix(String browserText) {
        for(String prefixName : builtInPrefixes.keySet()) {
            if(browserText.startsWith(prefixName)) {
                String prefix = builtInPrefixes.get(prefixName);
                return prefix + browserText.substring(prefixName.length());
            }
        }
        return browserText;
    }


    @Override
    public Set<E> getRenamedResult(Set<E> result, RenameMap renameMap) {
        return renameMap.getRenamedEntities(result);
    }

    /**
     * Creates any extra axiomsSource that are necessary to set up the "parent" association with the specified fresh entity.
     * @param freshEntity The fresh entity that was created. Not {@code null}.
     * @param context The change generation context. Not {@code null}.
     * @param parents The optional parents. Not {@code null}.
     * @return A possibly empty set of axiomsSource representing axiomsSource that need to be added to the project ontologies to
     * associate the specified fresh entity with its optional parent.  Not {@code null}.
     */
    protected abstract Set<? extends OWLAxiom> createParentPlacementAxioms(E freshEntity,
                                                                           ChangeGenerationContext context,
                                                                           ImmutableSet<P> parents);

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Set<E>> result) {
        Set<E> entities = result.getSubject();
        int entityCount = entities.size();
        String mainMsg;
        if(entityCount == 1) {
            mainMsg = msg.format("Created {0} {1}",
                                       entityType.getPrintName().toLowerCase(),
                                       entities);
            if (!parents.isEmpty()) {
                mainMsg += msg.format(" {0} {1}", getSingularRelationship(), parents);
            }
        }
        else {
            mainMsg = msg.format("Created {0} {1}",
                              entityType.getPluralPrintName().toLowerCase(),
                              entities);
            if (!parents.isEmpty()) {
                mainMsg += msg.format(" {0} {1}", getPluralRelationship(), parents);
            }
        }
        return mainMsg;
    }

    private String getSingularRelationship() {
        if(entityType.equals(CLASS)) {
            return "as a subclass of";
        }
        else if(entityType.equals(OBJECT_PROPERTY) ||
                entityType.equals(DATA_PROPERTY) ||
                entityType.equals(ANNOTATION_PROPERTY)) {
            return "as a sub-property of";
        }
        else if(entityType.equals(NAMED_INDIVIDUAL)) {
            return "as an instance of";
        }
        else if(entityType.equals(DATATYPE)) {
            return "as a subtype of";
        }
        else {
            return "as a child of";
        }
    }

    private String getPluralRelationship() {
        if(entityType.equals(CLASS)) {
            return "as subclasses of";
        }
        else if(entityType.equals(OBJECT_PROPERTY) ||
                entityType.equals(DATA_PROPERTY) ||
                entityType.equals(ANNOTATION_PROPERTY)) {
            return "as sub-properties of";
        }
        else if(entityType.equals(NAMED_INDIVIDUAL)) {
            return "as instances of";
        }
        else if(entityType.equals(DATATYPE)) {
            return "as subtypes of";
        }
        else {
            return "as children of";
        }
    }

    @Override
    public ChangeRequestId getChangeRequestId() {
        return changeRequestId;
    }
}
