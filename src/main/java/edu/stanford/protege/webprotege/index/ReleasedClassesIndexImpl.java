package edu.stanford.protege.webprotege.index;

import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ReleasedClassesIndexImpl implements ReleasedClassesIndex {

    @Nonnull
    private final Map<String, Boolean> map;

    @Inject
    public ReleasedClassesIndexImpl() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean isReleased(@NotNull OWLEntity entity) {
        if (entity.isOWLClass()) {
            return map.getOrDefault(entity.asOWLClass().getIRI().toString(), false);
        }
        return false;
    }
}
