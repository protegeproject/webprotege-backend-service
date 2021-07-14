package edu.stanford.protege.webprotege.crud.supplied;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.crud.EntityCrudKitId;
import edu.stanford.protege.webprotege.crud.EntityCrudKitSuffixSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static edu.stanford.protege.webprotege.crud.supplied.SuppliedNameSuffixSettings.TYPE_ID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
@AutoValue

@JsonTypeName(TYPE_ID)
public abstract class SuppliedNameSuffixSettings extends EntityCrudKitSuffixSettings {

    public static final String TYPE_ID = "SuppliedName";

    public static final String WHITE_SPACE_TREATMENT = "whiteSpaceTreatment";

    public static SuppliedNameSuffixSettings get() {
        return get(WhiteSpaceTreatment.TRANSFORM_TO_CAMEL_CASE);
    }

    @JsonCreator
    public static SuppliedNameSuffixSettings get(@Nullable @JsonProperty(WHITE_SPACE_TREATMENT) WhiteSpaceTreatment whiteSpaceTreatment) {
        if(whiteSpaceTreatment == null) {
            return get();
        }
        else {
            return new AutoValue_SuppliedNameSuffixSettings(whiteSpaceTreatment);
        }
    }

    @JsonIgnore
    @Override
    public EntityCrudKitId getKitId() {
        return SuppliedNameSuffixKit.getId();
    }

    @Nonnull
    public abstract WhiteSpaceTreatment getWhiteSpaceTreatment();
}
