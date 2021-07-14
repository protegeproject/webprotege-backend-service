package edu.stanford.protege.webprotege.match;



import edu.stanford.protege.webprotege.hierarchy.ClassHierarchyProvider;
import edu.stanford.protege.webprotege.match.criteria.HierarchyFilterType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */

public class SubClassOfMatcher implements Matcher<OWLEntity> {

    @Nonnull
    private final ClassHierarchyProvider provider;

    @Nonnull
    private final OWLClass cls;

    private final HierarchyFilterType filterType;

    @Inject
    public SubClassOfMatcher(@Nonnull ClassHierarchyProvider provider,
                             @Nonnull OWLClass cls,
                             @Nonnull HierarchyFilterType filterType) {
        this.provider = checkNotNull(provider);
        this.filterType = checkNotNull(filterType);
        this.cls = checkNotNull(cls);
    }

    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if(!value.isOWLClass()) {
            return false;
        }
        if(cls.isOWLThing()) {
            return true;
        }
        // Config for strict?
        if(value.equals(cls)) {
            return false;
        }
        if(filterType == HierarchyFilterType.DIRECT) {
            return provider.isParent(value.asOWLClass(), cls);
        }
        else {
            return provider.isAncestor(value.asOWLClass(), cls);
        }
    }
}
