package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.*;
import java.util.stream.Collectors;

public class FilteredClassHierarchyProvider implements ClassHierarchyProvider {

    private final ClassHierarchyProvider delegateProvider;

    private final OWLClass rootClass;

    private final Set<OWLClass> rootAncestors = new HashSet<>();

    protected FilteredClassHierarchyProvider(ClassHierarchyProvider delegateProvider, OWLClass rootClass) {
        this.delegateProvider = delegateProvider;
        this.rootClass = rootClass;
    }

    public static FilteredClassHierarchyProvider get(ClassHierarchyProvider delegateProvider,
                                                     OWLClass rootClass) {
        var provider = new FilteredClassHierarchyProvider(delegateProvider, rootClass);
        provider.init();
        return provider;
    }

    private void init() {
        rootAncestors.addAll(delegateProvider.getAncestors(rootClass));
    }

    @Override
    public void handleChanges(List<OntologyChange> changes) {
        // Don't do anything because changes are handled by the delegate
    }

    @Override
    public Collection<OWLClass> getRoots() {
        return List.of(rootClass);
    }

    @Override
    public Collection<OWLClass> getChildren(OWLClass object) {
        var inHierarchy = isInHierarchy(object);
        if(!inHierarchy) {
            return Collections.emptyList();
        }
        return delegateProvider.getChildren(object);
    }

    private boolean isInHierarchy(OWLClass object) {
        if(rootClass.isOWLThing()) {
            return true;
        }
        if(object.equals(rootClass)) {
            return true;
        }
        return delegateProvider.getPathsToRoot(object)
                .stream()
                .anyMatch(path -> path.contains(rootClass));
    }

    @Override
    public boolean isLeaf(OWLClass object) {
        var isLeaf = delegateProvider.isLeaf(object);
        if(!isLeaf) {
            return false;
        }
        return isInHierarchy(object);
    }

    @Override
    public Collection<OWLClass> getDescendants(OWLClass object) {
        if(isInHierarchy(object)) {
            return delegateProvider.getDescendants(object);
        }
        else {
            return List.of();
        }
    }

    @Override
    public Collection<OWLClass> getParents(OWLClass object) {
        if(object.equals(rootClass)) {
            return List.of();
        }
        return delegateProvider.getPathsToRoot(object)
                .stream()
                .filter(path -> path.contains(rootClass))
                .map(path -> path.get(path.size() - 1))
                .toList();
    }

    @Override
    public Collection<OWLClass> getAncestors(OWLClass object) {
        return getPathsToRoot(object)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<List<OWLClass>> getPathsToRoot(OWLClass object) {
        return delegateProvider.getPathsToRoot(object)
                .stream()
                .filter(path -> path.contains(rootClass))
                .map(path -> {
                    var startIndex = 0;
                    for(int i = 0; i < path.size() - 1; i++) {
                        if(rootAncestors.contains(path.get(i))) {
                            startIndex = i;
                        }
                    }
                    return path.subList(startIndex, path.size());
                })
                .toList();
    }

    @Override
    public boolean isAncestor(OWLClass descendant, OWLClass ancestor) {
        return isInHierarchy(descendant) && isInHierarchy(ancestor) && delegateProvider.isAncestor(descendant, ancestor);
    }
}
