package edu.stanford.protege.webprotege.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.entity.EntityNode;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */

@AutoValue
public abstract class EntitySearchResult {

    public static final String ENTITY = "entity";

    public static final String MATCHES = "matches";

    @Nonnull
    public static EntitySearchResult get(@JsonProperty(ENTITY) @Nonnull EntityNode entity,
                                         @JsonProperty(MATCHES) @Nonnull ImmutableList<SearchResultMatch> matches) {
        return new AutoValue_EntitySearchResult(entity, matches);
    }


    @JsonProperty(ENTITY)
    @Nonnull
    public abstract EntityNode getEntity();

    /**
     * Get the matches for this particular entity
     * @return The list of matches
     */
    @JsonProperty(MATCHES)
    @Nonnull
    public abstract ImmutableList<SearchResultMatch> getMatches();
}
