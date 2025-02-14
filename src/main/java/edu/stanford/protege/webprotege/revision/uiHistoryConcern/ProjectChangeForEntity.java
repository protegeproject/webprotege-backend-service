package edu.stanford.protege.webprotege.revision.uiHistoryConcern;

import edu.stanford.protege.webprotege.change.ProjectChange;
import org.jetbrains.annotations.NotNull;

public record ProjectChangeForEntity(String whoficEntityIri,
                                     ChangeType changeType,
                                     ProjectChange projectChange) implements Comparable<ProjectChangeForEntity> {

    public static ProjectChangeForEntity create(String whoficEntityIri,
                                                ChangeType changeType,
                                                ProjectChange projectChange) {
        return new ProjectChangeForEntity(whoficEntityIri, changeType, projectChange);
    }

    public static ProjectChangeForEntity create(String whoficEntityIri,
                                                ProjectChange projectChange) {
        return new ProjectChangeForEntity(whoficEntityIri, ChangeType.UPDATE_ENTITY, projectChange);
    }

    @Override
    public int compareTo(@NotNull ProjectChangeForEntity other) {
        int timestampComparison = Long.compare(this.projectChange.getTimestamp(), other.projectChange.getTimestamp());
        if (timestampComparison != 0) {
            return timestampComparison;
        }
        return this.whoficEntityIri.compareTo(other.whoficEntityIri);
    }

}
