package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class Watch implements IsSerializable {

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
