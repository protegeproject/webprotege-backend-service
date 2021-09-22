package edu.stanford.protege.webprotege.shortform;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public class SearchFiltersDocumentAugmenter implements EntityDocumentAugmenter {

    private final EntitySearchFilterMatchersFactory searchFilterMatchersFactory;

    @Inject
    public SearchFiltersDocumentAugmenter(@Nonnull EntitySearchFilterMatchersFactory searchFilterMatchersFactory) {
        this.searchFilterMatchersFactory = checkNotNull(searchFilterMatchersFactory);
    }


    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        var searchFilterMatchers = searchFilterMatchersFactory.getSearchFilterMatchers();
        if(searchFilterMatchers.isEmpty()) {
            return;
        }
        for(var searchFilterMatcher : searchFilterMatchers) {
            var searchFilter = searchFilterMatcher.getFilter();
            var matcher = searchFilterMatcher.getMatcher();
            if(matcher.matches(entity)) {
                var filterIdString = searchFilter.getId().getId();
                var field = toDocumentField(filterIdString);
                document.add(field);
            }
        }
    }

    private StringField toDocumentField(String filterId) {
        return new StringField(EntityDocumentFieldNames.SEARCH_FILTER_MATCHES,
                               filterId,
                               Field.Store.NO);
    }
}
