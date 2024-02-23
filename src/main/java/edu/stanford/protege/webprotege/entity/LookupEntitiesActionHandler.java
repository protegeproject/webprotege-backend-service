package edu.stanford.protege.webprotege.entity;

import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.app.PlaceUrl;
import edu.stanford.protege.webprotege.common.DictionaryLanguage;
import edu.stanford.protege.webprotege.common.PageRequest;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.lang.LanguageManager;
import edu.stanford.protege.webprotege.match.Matcher;
import edu.stanford.protege.webprotege.match.MatcherFactory;
import edu.stanford.protege.webprotege.search.EntityNameMatchResult;
import edu.stanford.protege.webprotege.search.SearchResultMatch;
import edu.stanford.protege.webprotege.search.SearchResultMatchPosition;
import edu.stanford.protege.webprotege.shortform.DictionaryManager;
import edu.stanford.protege.webprotege.shortform.SearchString;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
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
    protected BuiltInAction getRequiredExecutableBuiltInAction(LookupEntitiesAction action) {
        return BuiltInAction.VIEW_PROJECT;
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

        var result = dictionaryManager.getShortFormsContaining(searchStrings,
                                                               entityLookupRequest.getSearchedEntityTypes(),
                                                               languageManager.getLanguages(), ImmutableList.of(),
                                                               PageRequest.requestFirstPage());

        List<EntityLookupResult> lookupResults = new ArrayList<>();
        for (var match : result.getPageElements()) {
            var matchedEntity = match.getEntity();
            if (!addedEntities.contains(matchedEntity) && matcher.matches(matchedEntity)) {
                addedEntities.add(matchedEntity);
                for (int i = 0; i < 1 ; i++) {
                    var shortFormMatch = match.getShortFormMatches().get(i);
                    var language = shortFormMatch.getLanguage();
                    var matchPositions = shortFormMatch.getMatchPositions()
                            .stream()
                            .map(p -> SearchResultMatchPosition.get(p.getStart(), p.getEnd()))
                            .collect(toImmutableList());
                    var node = entityNodeRenderer.render(matchedEntity);
                    var matchResult = SearchResultMatch.get(node,
                                                            shortFormMatch.getLanguage(),
                                                            shortFormMatch.getShortForm(),
                                                            matchPositions);
                    String entityUrl = getEntityUrl(matchedEntity);
                    var entityLookupResult = EntityLookupResult.get(matchResult, entityUrl);
                    lookupResults.add(entityLookupResult);
                }

            }
        }
        return lookupResults;
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
