package edu.stanford.protege.webprotege.hierarchy;

import edu.stanford.protege.webprotege.icd.actions.AncestorHierarchyNode;
import org.semanticweb.owlapi.model.*;

import java.util.*;

public class HierarchyProviderGuard<E extends OWLEntity> implements HierarchyProvider<OWLEntity> {

    private final Class<E> cls;

    private final HierarchyProvider<E> delegate;

    public HierarchyProviderGuard(Class<E> cls, HierarchyProvider<E> delegate) {
        this.cls = cls;
        this.delegate = delegate;
    }

    @Override
    public Collection<OWLEntity> getRoots() {
        var roots = delegate.getRoots();
        return  Collections.unmodifiableCollection(roots);
    }

    @Override
    public Collection<OWLEntity> getChildren(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(delegate.getChildren(cls.cast(object)));
    }

    @Override
    public boolean isLeaf(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return false;
        }
        return delegate.isLeaf(cls.cast(object));
    }

    @Override
    public Collection<OWLEntity> getDescendants(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(delegate.getDescendants(cls.cast(object)));
    }

    @Override
    public Collection<OWLEntity> getParents(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(delegate.getParents(cls.cast(object)));
    }

    @Override
    public Collection<OWLEntity> getAncestors(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(delegate.getAncestors(cls.cast(object)));
    }

    @Override
    public Collection<List<OWLEntity>> getPathsToRoot(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return Collections.emptyList();
        }
        return delegate.getPathsToRoot(cls.cast(object))
                .stream()
                .map(path -> (List<OWLEntity>) new ArrayList<OWLEntity>(path))
                .toList();
    }

    @Override
    public boolean isAncestor(OWLEntity descendant, OWLEntity ancestor) {
        if(!cls.isInstance(descendant)) {
            return false;
        }
        if(!cls.isInstance(ancestor)) {
            return false;
        }
        return delegate.isAncestor(cls.cast(descendant), cls.cast(ancestor));
    }

    @Override
    public boolean contains(Object object) {
        if(!cls.isInstance(object)) {
            return false;
        }
        return delegate.contains(object);
    }

    @Override
    public AncestorHierarchyNode<OWLEntity> getAncestorsTree(OWLEntity object) {
        if(!cls.isInstance(object)) {
            return (AncestorHierarchyNode<OWLEntity>) delegate.getAncestorsTree(cls.cast(object));
        }
       return new AncestorHierarchyNode<>();
    }
}
