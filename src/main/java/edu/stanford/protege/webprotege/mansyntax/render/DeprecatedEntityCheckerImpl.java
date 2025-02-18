package edu.stanford.protege.webprotege.mansyntax.render;

import edu.stanford.protege.webprotege.index.DeprecatedEntitiesByEntityIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@ProjectSingleton
public class DeprecatedEntityCheckerImpl implements DeprecatedEntityChecker {

    @Nonnull
    private final DeprecatedEntitiesByEntityIndex deprecatedEntitiesByEntityIndex;

    @Inject
    public DeprecatedEntityCheckerImpl(@Nonnull DeprecatedEntitiesByEntityIndex deprecatedEntitiesByEntityIndex) {
        this.deprecatedEntitiesByEntityIndex = checkNotNull(deprecatedEntitiesByEntityIndex);
    }

    @Override
    public boolean isDeprecated(OWLEntity entity) {
        return deprecatedEntitiesByEntityIndex.isDeprecated(entity);
    }
}
