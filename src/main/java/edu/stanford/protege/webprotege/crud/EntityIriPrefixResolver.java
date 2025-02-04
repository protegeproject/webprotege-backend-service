package edu.stanford.protege.webprotege.crud;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.match.HierarchyPositionCriteriaMatchableEntityTypesExtractor;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public class EntityIriPrefixResolver {

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Nonnull
    private final EntityIriPrefixCriteriaRewriter entityIriPrefixCriteriaRewriter;

    @Nonnull
    private final HierarchyPositionCriteriaMatchableEntityTypesExtractor leafCriteriaTypesExtractor;

    @Inject
    public EntityIriPrefixResolver(@Nonnull MatcherFactory matcherFactory,
                                   @Nonnull EntityIriPrefixCriteriaRewriter entityIriPrefixCriteriaRewriter,
                                   @Nonnull HierarchyPositionCriteriaMatchableEntityTypesExtractor leafCriteriaTypesExtractor) {
        this.matcherFactory = matcherFactory;
        this.entityIriPrefixCriteriaRewriter = entityIriPrefixCriteriaRewriter;
        this.leafCriteriaTypesExtractor = leafCriteriaTypesExtractor;
    }

    /**
     * Gets the IRI prefix for the specified prefix settings and the specified
     * parents.
     * @param prefixSettings The prefix settings.
     * @param entityType The entity type of the entity to be created.
     * @param parentEntities The parent entities.  May be empty.  If the parents is empty
     *                       then the fallback prefix will be returned.
     * @return The resolved prefix.
     */
    @Nonnull
    public String getIriPrefix(@Nonnull EntityCrudKitPrefixSettings prefixSettings,
                               @Nonnull EntityType<?> entityType,
                               @Nonnull ImmutableList<OWLEntity> parentEntities) {
        if(parentEntities.isEmpty()) {
            return prefixSettings.getIRIPrefix();
        }
        var conditionalPrefixes = prefixSettings.getConditionalIriPrefixes();
        for(var conditionalPrefix : conditionalPrefixes) {
            var leafTypes = leafCriteriaTypesExtractor.getMatchableEntityTypes(conditionalPrefix.getCriteria());
            if (leafTypes.contains(entityType)) {
                if(allEntitiesMatch(parentEntities, conditionalPrefix)) {
                    return conditionalPrefix.getIriPrefix();
                }
            }
        }
        return prefixSettings.getIRIPrefix();
    }

    private boolean allEntitiesMatch(@Nonnull ImmutableList<OWLEntity> parentEntities,
                                     @Nonnull ConditionalIriPrefix conditionalPrefix) {
        var criteria = conditionalPrefix.getCriteria();
        var rewrittenCriteria = entityIriPrefixCriteriaRewriter.rewriteCriteria(criteria);
        var matcher = matcherFactory.getMatcher(rewrittenCriteria);
        for(var parent : parentEntities) {
            if(!matcher.matches(parent)) {
                return false;
            }
        }
        return true;
    }
}
