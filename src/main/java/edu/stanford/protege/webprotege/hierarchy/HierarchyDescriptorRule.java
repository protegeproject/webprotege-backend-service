package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.forms.field.FormRegionId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;
import edu.stanford.protege.webprotege.ui.ViewId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record HierarchyDescriptorRule(
        @Nullable @JsonProperty("requiredPerspectiveId") PerspectiveId requiredPerspectiveId,
        @Nullable @JsonProperty("requiredViewId") ViewId requiredViewId,
        @Nonnull @JsonProperty("requiredViewProperties") Map<String, String> requiredViewProperties,
        @Nullable @JsonProperty("requiredFormId") FormId requiredFormId,
        @Nullable @JsonProperty("requiredFormFieldId") FormRegionId requiredFormFieldId,
        @Nonnull @JsonProperty("requiredCapabilities") @JsonAlias("requiredActions") Set<Capability> requiredCapabilities,
        @Nonnull @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {


    /**
     * Creates a {@code HierarchyDescriptorRule} with empty sets and an empty map for all matching criteria
     * and the specified {@link HierarchyDescriptor}.  This rule will always match any criteria.
     * @param hierarchyDescriptor the {@link HierarchyDescriptor} associated with this rule
     * @return a new {@code HierarchyDescriptorRule} with empty sets, an empty map, and the given {@link HierarchyDescriptor}
     * @throws NullPointerException if {@code hierarchyDescriptor} is {@code null}
     */
    public static HierarchyDescriptorRule create(HierarchyDescriptor hierarchyDescriptor) {
        return new HierarchyDescriptorRule(
                null, null, Collections.<String, String>emptyMap(),
                null,
                null,
                Collections.emptySet(),
                Objects.requireNonNull(hierarchyDescriptor, "hierarchyDescriptor must not be null")
        );
    }
}
