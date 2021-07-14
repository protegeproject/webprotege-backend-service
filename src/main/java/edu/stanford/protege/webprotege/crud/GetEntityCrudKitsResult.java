package edu.stanford.protege.webprotege.crud;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
@AutoValue

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
