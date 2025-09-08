package edu.stanford.protege.webprotege.hierarchy;

import com.google.common.base.Stopwatch;
import edu.stanford.protege.webprotege.DataFactory;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.index.*;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 17-Jan-2007<br><br>
 */
public class ClassHierarchyProviderImpl extends AbstractHierarchyProvider<OWLClass> implements ClassHierarchyProvider {

    private static final Logger logger = LoggerFactory.getLogger(ClassHierarchyProviderImpl.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Set<OWLClass> roots;

    @Nonnull
    private final TerminalElementFinder<OWLClass> rootFinder;

    @Nonnull
    private final Set<OWLClass> nodesToUpdate = new HashSet<>();

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;

    @Nonnull
    private final ProjectSignatureByTypeIndex projectSignatureByTypeIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Nonnull
    private final ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex;

    private boolean stale = true;

    @Inject
    public ClassHierarchyProviderImpl(ProjectId projectId,
                                      @Nonnull @ClassHierarchyRoot Set<OWLClass> roots,
                                      @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                      @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex,
                                      @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                      @Nonnull ProjectSignatureByTypeIndex projectSignatureByTypeIndex,
                                      @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                      @Nonnull ClassHierarchyChildrenAxiomsIndex classHierarchyChildrenAxiomsIndex) {
        this.projectId = checkNotNull(projectId);
        this.roots = new LinkedHashSet<>(checkNotNull(roots));
        checkRoots(roots);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsIndex = subClassOfAxiomsIndex;
        this.equivalentClassesAxiomsIndex = equivalentClassesAxiomsIndex;
        this.projectSignatureByTypeIndex = projectSignatureByTypeIndex;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.classHierarchyChildrenAxiomsIndex = classHierarchyChildrenAxiomsIndex;
        rootFinder = new TerminalElementFinder<>(this::getParents);
        nodesToUpdate.clear();
    }

    private static void checkRoots(Set<OWLClass> roots) {
        if(roots.isEmpty()) {
            throw new RuntimeException("Bad specification of root classes in class hierarchy.  No root classes have been specified.");
        }
        if(roots.stream().anyMatch(OWLClass::isOWLThing)) {
            if(roots.size() != 1) {
                throw new RuntimeException("Bad specification of root classes in class hierarchy.  Specified root classes: " + roots + ".  If owl:Thing is specified as a root then it must be the one and only root.");
            }
        }

    }


    public synchronized Collection<OWLClass> getParents(OWLClass object) {
        rebuildIfNecessary();
        // If the object is the root then there
        // are no parents
        if(roots.contains(object)) {
            return Collections.emptySet();
        }
        Stream<OWLClass> parentsCombined = getParentsStream(object);
        var parents = parentsCombined.collect(toSet());
        // Thing if the object is a root class
        if (hasOwlThingAsRoot()) {
            // Add orphans, by the semantics of OWL
            if (rootFinder.getTerminalElements()
                    .contains(object)) {
                parents.add(DataFactory.getOWLThing());
            }
        }
        return parents;
    }

    private boolean hasOwlThingAsRoot() {
        return roots.stream().anyMatch(OWLClass::isOWLThing);
    }

    @Override
    public boolean isParent(OWLClass child, OWLClass parent) {
        return getParentsStream(child).anyMatch(c -> c.equals(parent));
    }

    private Stream<OWLClass> getParentsStream(OWLClass object) {
        var subClassOfAxiomsParents =
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(object,
                                                                                                             ontId))
                                      .map(OWLSubClassOfAxiom::getSuperClass)
                                      .flatMap(this::asConjunctSet)
                                      .filter(OWLClassExpression::isNamed)
                                      .map(OWLClassExpression::asOWLClass);


        var equivalentClassesAxiomsParents =
                projectOntologiesIndex.getOntologyIds()
                                      .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(
                                              object,
                                              ontId))
                                      .flatMap(ax -> ax.getClassExpressions()
                                                       .stream())
                                      .filter(ce -> !ce.equals(object))
                                      .flatMap(this::asConjunctSet)
                                      .filter(OWLClassExpression::isNamed)
                                      .map(OWLClassExpression::asOWLClass);

        return Stream.concat(subClassOfAxiomsParents, equivalentClassesAxiomsParents);
    }

    private void rebuildIfNecessary() {
        if(stale) {
            rebuildImplicitRoots();
        }
    }

    private Stream<OWLClassExpression> asConjunctSet(@Nonnull OWLClassExpression cls) {
        if(cls instanceof OWLObjectIntersectionOf) {
            return ((OWLObjectIntersectionOf) cls).getOperandsAsList()
                                           .stream()
                                           .flatMap(this::asConjunctSet);
        }
        else {
            return Stream.of(cls);
        }
    }

    private void rebuildImplicitRoots() {
        stale = false;
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("{} Rebuilding class hierarchy", projectId);
        rootFinder.clear();
        var signature = projectSignatureByTypeIndex.getSignature(EntityType.CLASS)
                                                   .collect(toImmutableSet());
        rootFinder.appendTerminalElements(signature);
        rootFinder.finish();
        logger.info("{} Rebuilt class hierarchy in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
    }

    public void dispose() {
    }

    public synchronized void handleChanges(@Nonnull List<OntologyChange> changes) {
        stale = true;
        Set<OWLClass> oldTerminalElements = new HashSet<>(rootFinder.getTerminalElements());
        Set<OWLClass> changedClasses = new HashSet<>(roots);
        var filteredChanges = filterIrrelevantChanges(changes);
        updateImplicitRoots(filteredChanges);
        for(OntologyChange change : filteredChanges) {
            changedClasses.addAll(change.getSignature()
                                        .stream()
                                        .filter(OWLEntity::isOWLClass)
                                        .map(OWLEntity::asOWLClass)
                                        .filter(entity -> !roots.contains(entity))
                                        .map(entity -> (OWLClass) entity)
                                        .toList());
        }
        changedClasses.forEach(this::registerNodeChanged);
        rootFinder.getTerminalElements()
                  .stream()
                  .filter(cls -> !oldTerminalElements.contains(cls))
                  .forEach(this::registerNodeChanged);
        oldTerminalElements.stream()
                           .filter(cls -> !rootFinder.getTerminalElements()
                                                     .contains(cls))
                           .forEach(this::registerNodeChanged);
        notifyNodeChanges();
    }

    private List<OntologyChange> filterIrrelevantChanges(List<OntologyChange> changes) {
        return changes.stream()
                      .filter(OntologyChange::isAxiomChange)
                      .collect(toList());
    }

    private void updateImplicitRoots(List<OntologyChange> changes) {
        if(!hasOwlThingAsRoot()) {
            return;
        }
        Set<OWLClass> possibleTerminalElements = new HashSet<>();
        Set<OWLClass> notInOntologies = new HashSet<>();

        // only listen for changes on the appropriate ontologies
        changes.stream()
               .filter(OntologyChange::isAxiomChange)
               .forEach(change -> {
                   boolean remove = change.isRemoveAxiom();
                   var axiom = change.getAxiomOrThrow();
                   axiom.getSignature()
                        .stream()
                        .filter(OWLEntity::isOWLClass)
                           .map(OWLEntity::asOWLClass)
                        .filter(entity -> !roots.contains(entity))
                        .forEach(entity -> {
                            if(!remove || containsReference(entity)) {
                                possibleTerminalElements.add(entity);
                            }
                            else {
                                notInOntologies.add(entity);
                            }
                        });
               });

        possibleTerminalElements.addAll(rootFinder.getTerminalElements());
        possibleTerminalElements.removeAll(notInOntologies);
        rootFinder.findTerminalElements(possibleTerminalElements);
    }

    private void registerNodeChanged(OWLClass node) {
        nodesToUpdate.add(node);
    }

    private void notifyNodeChanges() {
        nodesToUpdate.clear();
    }

    public synchronized boolean containsReference(OWLClass object) {
        if(roots.contains(object)) {
            return true;
        }
        var containsInSig = entitiesInProjectSignatureByIriIndex
                .getEntitiesInSignature(object.getIRI())
                .anyMatch(entity -> entity.equals(object));
        if(!containsInSig) {
            return false;
        }
        if(hasOwlThingAsRoot()) {
            return true;
        }
        else {
            return !getPathsToRoot(object).isEmpty();
        }
    }

    public synchronized Collection<OWLClass> getRoots() {
        rebuildIfNecessary();
        return Set.copyOf(roots);
    }

    public synchronized Collection<OWLClass> getChildren(OWLClass object) {
        rebuildIfNecessary();
        Set<OWLClass> result;
        if(roots.contains(object)) {
            result = new HashSet<>();
            if (hasOwlThingAsRoot()) {
                result.addAll(rootFinder.getTerminalElements());
            }
            result.addAll(extractChildren(object));
            result.remove(object);
        }
        else {
            result = extractChildren(object);
            //            result.removeIf(curChild -> getAncestors(object).contains(curChild));
        }

        return result;
    }

    private Set<OWLClass> extractChildren(OWLClass parent) {
        return classHierarchyChildrenAxiomsIndex.getChildrenAxioms(parent)
                                         .flatMap(ax -> {
                                             if(ax instanceof OWLSubClassOfAxiom) {
                                                 return Stream.of(((OWLSubClassOfAxiom) ax).getSubClass().asOWLClass());
                                             }
                                             else if(ax instanceof OWLEquivalentClassesAxiom) {
                                                 return ((OWLEquivalentClassesAxiom) ax).getClassExpressionsAsList()
                                                         .stream()
                                                         .filter(IsAnonymous::isNamed)
                                                         .map(ce -> (OWLClass) ce);
                                             }
                                             else {
                                                 return Stream.empty();
                                             }
                                         })
                                         .collect(toImmutableSet());
    }

    @Override
    public boolean isLeaf(OWLClass object) {
        return classHierarchyChildrenAxiomsIndex.isLeaf(object);
    }

    @Override
    public boolean contains(Object object) {
        if(!(object instanceof OWLClass)) {
            return false;
        }
        return containsReference((OWLClass) object);
    }
}
