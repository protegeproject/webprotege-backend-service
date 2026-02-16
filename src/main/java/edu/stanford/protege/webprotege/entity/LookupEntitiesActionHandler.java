package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInCapability;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.EntityTypeIsOneOfCriteria;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.match.Matcher;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.search.DeprecatedEntitiesTreatment;
import edu.stanford.protege.webprotege.search.EntityNameMatchResult;
import edu.stanford.protege.webprotege.search.SearchResultMatch;
import edu.stanford.protege.webprotege.search.SearchResultMatchPosition;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import edu.stanford.protege.webprotege.shortform.SearchString;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesActionHandler extends AbstractProjectActionHandler<LookupEntitiesAction, LookupEntitiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final PlaceUrl placeUrl;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public LookupEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull PlaceUrl placeUrl,
                                       @Nonnull EntityNodeRenderer entityNodeRenderer,
                                       @Nonnull DictionaryManager dictionaryManager,
                                       @Nonnull LanguageManager languageManager,
                                       @Nonnull MatcherFactory matcherFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.placeUrl = placeUrl;
        this.entityNodeRenderer = entityNodeRenderer;
        this.dictionaryManager = dictionaryManager;
        this.languageManager = languageManager;
        this.matcherFactory = matcherFactory;
    }

    @Nonnull
    @Override
    public Class<LookupEntitiesAction> getActionClass() {
        return LookupEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInCapability getRequiredExecutableBuiltInAction(LookupEntitiesAction action) {
        return BuiltInCapability.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public LookupEntitiesResult execute(@Nonnull LookupEntitiesAction action,
                                        @Nonnull ExecutionContext executionContext) {
        return new LookupEntitiesResult(lookupEntities(action.entityLookupRequest()));
    }


    private List<EntityLookupResult> lookupEntities(final EntityLookupRequest entityLookupRequest) {
        Matcher<OWLEntity> matcher = entityLookupRequest.getEntityMatchCriteria()
                                                        .map(matcherFactory::getMatcher)
                                                        .orElse(entity -> true);
        Set<OWLEntity> addedEntities = new HashSet<>();
        List<SearchString> searchStrings = SearchString.parseMultiWordSearchString(entityLookupRequest.getSearchString());
        int searchLimit = entityLookupRequest.getSearchLimit();

        try {
            var matchesStream = dictionaryManager.getShortFormsContainingAsStream(
                    searchStrings,
                    entityLookupRequest.getSearchedEntityTypes(),
                    languageManager.getLanguages(),
                    ImmutableList.of(),
                    EntityTypeIsOneOfCriteria.get(ImmutableSet.copyOf(entityLookupRequest.getSearchedEntityTypes())),
                    DeprecatedEntitiesTreatment.INCLUDE_DEPRECATED_ENTITIES
            );

            return matchesStream
                    .filter(match -> matcher.matches(match.getEntity()))
                    .filter(match -> {
                        var matchedEntity = match.getEntity();
                        if (addedEntities.contains(matchedEntity)) {
                            return false;
                        }
                        addedEntities.add(matchedEntity);
                        return true;
                    })
                    .limit(searchLimit)
                    .filter(match -> !match.getShortFormMatches().isEmpty())
                    .map(match -> {
                        var shortFormMatch = match.getShortFormMatches().get(0);
                        var matchPositions = shortFormMatch.getMatchPositions()
                                .stream()
                                .map(p -> SearchResultMatchPosition.get(p.getStart(), p.getEnd()))
                                .collect(toImmutableList());
                        var node = entityNodeRenderer.render(match.getEntity());
                        var matchResult = SearchResultMatch.get(node,
                                                                shortFormMatch.getLanguage(),
                                                                shortFormMatch.getShortForm(),
                                                                matchPositions);
                        String entityUrl = getEntityUrl(match.getEntity());
                        return EntityLookupResult.get(matchResult, entityUrl);
                    })
                    .collect(toImmutableList());
        } catch (IOException e) {
            // Log error and return empty list
            return ImmutableList.of();
        }
    }

    @Nonnull
    private String getEntityUrl(OWLEntity matchedEntity) {
        try {
            return placeUrl.getEntityUrl(projectId, matchedEntity);
        } catch (Exception e) {
            return "";
        }
    }

    private static class OWLEntityDataMatch implements Comparable<OWLEntityDataMatch> {

        private final DictionaryLanguage dictionaryLanguage;

        private final OWLEntityData visualEntity;

        private final EntityNameMatchResult matchResult;

        private OWLEntityDataMatch(DictionaryLanguage dictionaryLanguage,
                                   OWLEntityData visualEntity,
                                   EntityNameMatchResult matchResult) {
            this.dictionaryLanguage = dictionaryLanguage;
            this.visualEntity = visualEntity;
            this.matchResult = matchResult;
        }

        public DictionaryLanguage getDictionaryLanguage() {
            return dictionaryLanguage;
        }

        public OWLEntityData getEntityData() {
            return visualEntity;
        }

        private EntityNameMatchResult getMatchResult() {
            return matchResult;
        }

        @Override
        public int compareTo(@Nonnull OWLEntityDataMatch owlEntityDataMatch) {
            int diff = this.matchResult.compareTo(owlEntityDataMatch.matchResult);
            if (diff != 0) {
                return diff;
            }
            return visualEntity.compareToIgnorePrefixNames(owlEntityDataMatch.getEntityData());
        }
    }

}
