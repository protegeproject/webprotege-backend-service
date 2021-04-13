package edu.stanford.bmir.protege.web.shared.individuals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetIndividuals")
public abstract class GetIndividualsResult implements Result {

    @JsonCreator
    public static GetIndividualsResult create(@JsonProperty("type") Optional<OWLClassData> type,
                                              @JsonProperty("individuals") Page<EntityNode> result) {
        return new AutoValue_GetIndividualsResult(type.orElse(null), result);
    }

    public Optional<OWLClassData> getType() {
        return Optional.ofNullable(getTypeInternal());
    }

    @JsonIgnore
    @Nullable
    public abstract OWLClassData getTypeInternal();

    public abstract Page<EntityNode> getIndividuals();
}
