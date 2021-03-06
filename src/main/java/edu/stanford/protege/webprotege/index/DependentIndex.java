package edu.stanford.protege.webprotege.index;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-11
 */
public interface DependentIndex extends Index {

    @Nonnull
    Collection<Index> getDependencies();
}
