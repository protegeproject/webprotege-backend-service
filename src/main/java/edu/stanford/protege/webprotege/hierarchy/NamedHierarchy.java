package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.common.LanguageMap;

import javax.annotation.Nonnull;

public record NamedHierarchy(@Nonnull
                             @JsonProperty("hierarchyId") HierarchyId hierarchyId,
                             @Nonnull
                             @JsonProperty("label") LanguageMap label,
                             @Nonnull
                             @JsonProperty("description") LanguageMap description,
                             @Nonnull
                             @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {
}
