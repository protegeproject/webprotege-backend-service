package edu.stanford.protege.webprotege.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@AutoValue

public abstract class Watch {

    public static final String USER_ID = "userId";

    public static final String ENTITY = "entity";

    public static final String TYPE = "type";

    @JsonCreator
    public static Watch create(@JsonProperty(USER_ID) @Nonnull UserId userId,
                 @JsonProperty(ENTITY) @Nonnull OWLEntity entity,
                 @JsonProperty(TYPE) @Nonnull WatchType type) {
        return new AutoValue_Watch(userId, entity, type);
    }

    @Nonnull
    public abstract UserId getUserId();

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract WatchType getType();
}
