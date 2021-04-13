package edu.stanford.bmir.protege.web.shared.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetEntityCrudKits")
public abstract class GetEntityCrudKitsResult implements Result {


    @JsonCreator
    public static GetEntityCrudKitsResult create(@JsonProperty("kits") List<EntityCrudKit<?>> kits,
                                                 @JsonProperty("currentSettings") EntityCrudKitSettings<?> currentSettings) {
        return new AutoValue_GetEntityCrudKitsResult(kits, currentSettings);
    }

    public abstract List<EntityCrudKit<?>> getKits();

    public abstract EntityCrudKitSettings<?> getCurrentSettings();
}
