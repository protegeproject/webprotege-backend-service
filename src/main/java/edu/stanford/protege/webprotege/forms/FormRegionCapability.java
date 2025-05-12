package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.GenericParameterizedCapability;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.criteria.EntityMatchCriteria;
import edu.stanford.protege.webprotege.criteria.MultiMatchType;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonTypeName(FormRegionCapability.TYPE)
public record FormRegionCapability(@JsonProperty("id") String id,
                                   @JsonProperty("formRegionId") FormRegionId formRegionId,
                                   @JsonProperty("contextCriteria") CompositeRootCriteria contextCriteria) implements Capability {

    public static final String TYPE = "FormRegionCapability";

    public static final String VIEW_FORM_REGION = "ViewFormRegion";

    public static final String EDIT_FORM_REGION = "EditFormRegion";

    public static FormRegionCapability valueOf(String id, FormRegionId formRegionId) {
        return new FormRegionCapability(id, formRegionId, anyContext());
    }

    @JsonCreator
    public static FormRegionCapability valueOf(@JsonProperty("id") String id,
                                               @JsonProperty("formRegionId") FormRegionId formRegionId,
                                               @JsonProperty("contextCriteria") @Nullable CompositeRootCriteria contextCriteria) {
        return new FormRegionCapability(id, formRegionId, Objects.requireNonNullElse(contextCriteria, anyContext()));
    }

    private static @NotNull CompositeRootCriteria anyContext() {
        return CompositeRootCriteria.get(List.of(), MultiMatchType.ANY);
    }

    @Override
    public GenericParameterizedCapability asGenericCapability() {
        return new GenericParameterizedCapability(TYPE, id, Map.of("formRegion", formRegionId));
    }
}
