package edu.stanford.protege.webprotege.merge;

import com.google.common.collect.ImmutableSet;

import java.io.Serializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class Diff<T extends Serializable> implements  Serializable {

    private ImmutableSet<T> added;

    private ImmutableSet<T> removed;

    private Diff() {
    }

    public Diff(ImmutableSet<T> added, ImmutableSet<T> removed) {
        this.added = added;
        this.removed = removed;
    }
    

    public ImmutableSet<T> getAdded() {
        return added;
    }

    public ImmutableSet<T> getRemoved() {
        return removed;
    }

    public boolean isEmpty() {
        return added.isEmpty() && removed.isEmpty();
    }


}
