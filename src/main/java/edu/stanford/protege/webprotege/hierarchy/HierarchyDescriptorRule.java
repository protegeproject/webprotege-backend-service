package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.forms.FormId;
import edu.stanford.protege.webprotege.perspective.PerspectiveId;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a rule used to select a hierarchy descriptor based on matching criteria
 * for action IDs, perspective IDs, and form IDs. Each criterion is evaluated as a disjunction
 * (logical OR) of the elements in the corresponding set.
 *
 * @param matchedActions        the set of {@link ActionId} instances that are matched by this rule;
 *                              the rule matches if any of the {@link ActionId}s in the set is satisfied
 *                              (disjunction).  The rule will match ANY {@link ActionId} if the set is empty.
 * @param matchedPerspectiveIds the set of {@link PerspectiveId} instances that are matched by this rule;
 *                              the rule matches if any of the {@link PerspectiveId}s in the set is satisfied
 *                              (disjunction).  he rule will match ANY {@link PerspectiveId} if the set is empty.
 * @param matchedFormIds        the set of {@link FormId} instances that are matched by this rule;
 *                              the rule matches if any of the {@link FormId}s in the set is satisfied
 *                              (disjunction).  he rule will match ANY {@link FormId} if the set is empty.
 * @param hierarchyDescriptor   the {@link HierarchyDescriptor} associated with and selected by this rule.
 */
public record HierarchyDescriptorRule(
        @JsonProperty("matchedActions") Set<ActionId> matchedActions,
        @JsonProperty("matchedPerspectiveIds") Set<PerspectiveId> matchedPerspectiveIds,
        @JsonProperty("matchedFormIds") Set<FormId> matchedFormIds,
        @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {

    /**
     * Constructs a {@code HierarchyDescriptorRule} while ensuring that all parameters are non-null.
     *
     * @param matchedActions        the set of {@link ActionId} instances that are matched by this rule
     * @param matchedPerspectiveIds the set of {@link PerspectiveId} instances that are matched by this rule
     * @param matchedFormIds        the set of {@link FormId} instances that are matched by this rule
     * @param hierarchyDescriptor   the {@link HierarchyDescriptor} associated with this rule
     * @throws NullPointerException if any parameter is {@code null}
     */
    public HierarchyDescriptorRule(@JsonProperty("matchedActions") Set<ActionId> matchedActions,
                                   @JsonProperty("matchedPerspectiveIds") Set<PerspectiveId> matchedPerspectiveIds,
                                   @JsonProperty("matchedFormIds") Set<FormId> matchedFormIds,
                                   @JsonProperty("hierarchyDescriptor") HierarchyDescriptor hierarchyDescriptor) {
        this.matchedActions = Objects.requireNonNull(matchedActions, "matchedActions must not be null");
        this.matchedPerspectiveIds = Objects.requireNonNull(matchedPerspectiveIds, "matchedPerspectiveIds must not be null");
        this.matchedFormIds = Objects.requireNonNull(matchedFormIds, "matchedFormIds must not be null");
        this.hierarchyDescriptor = Objects.requireNonNull(hierarchyDescriptor, "hierarchyDescriptor must not be null");
    }

    /**
     * Creates a {@code HierarchyDescriptorRule} with empty sets for all matching criteria
     * and the specified {@link HierarchyDescriptor}.  This rule will always match any criteria.
     *
     * @param hierarchyDescriptor the {@link HierarchyDescriptor} associated with this rule
     * @return a new {@code HierarchyDescriptorRule} with empty sets and the given {@link HierarchyDescriptor}
     * @throws NullPointerException if {@code hierarchyDescriptor} is {@code null}
     */
    public static HierarchyDescriptorRule create(HierarchyDescriptor hierarchyDescriptor) {
        return new HierarchyDescriptorRule(
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet(),
                Objects.requireNonNull(hierarchyDescriptor, "hierarchyDescriptor must not be null")
        );
    }
}
