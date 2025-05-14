package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.authorization.RoleId;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;

import java.util.Objects;

public record FormRegionAccessRestriction(@JsonProperty("formRegionId") FormRegionId formRegionId,
                                          @JsonProperty("roleId") RoleId roleId,
                                          @JsonProperty("capabilityId") String capabilityId,
                                          @JsonProperty("criteria") CompositeRootCriteria criteria) {

    public FormRegionAccessRestriction(@JsonProperty("formRegionId" ) FormRegionId formRegionId,
                                       @JsonProperty("roleId" ) RoleId roleId,
                                       @JsonProperty("capabilityId" ) String capabilityId,
                                       @JsonProperty("criteria") CompositeRootCriteria criteria) {
        this.formRegionId = Objects.requireNonNull(formRegionId);
        this.roleId = Objects.requireNonNull(roleId);
        this.capabilityId = Objects.requireNonNull(capabilityId);
        this.criteria = Objects.requireNonNull(criteria);
    }
}
