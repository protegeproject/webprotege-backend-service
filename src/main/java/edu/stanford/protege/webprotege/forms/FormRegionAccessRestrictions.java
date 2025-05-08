package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.SetMultimap;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

public record FormRegionAccessRestrictions(@JsonProperty("formRegionId") FormRegionId formRegionId,
                                           @JsonProperty("capabilityRoles") SetMultimap<String, RoleId> capabilityRoles) {
}
