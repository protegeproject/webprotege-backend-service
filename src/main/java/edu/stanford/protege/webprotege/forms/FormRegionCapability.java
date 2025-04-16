package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.GenericParameterizedCapability;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import java.util.Map;

@JsonTypeName(FormRegionCapability.TYPE)
public record FormRegionCapability(String id, FormRegionId formRegionId) implements Capability {

    public static final String TYPE = "FormRegionCapability";

    public static final String VIEW_FORM_REGION = "ViewFormRegion";

    public static final String EDIT_FORM_REGION = "EditFormRegion";

    public static FormRegionCapability valueOf(String id, FormRegionId formRegionId) {
        return new FormRegionCapability(id, formRegionId);
    }

    @Override
    public GenericParameterizedCapability asGenericCapability() {
        return new GenericParameterizedCapability(TYPE, id, Map.of("formRegion", formRegionId));
    }
}
