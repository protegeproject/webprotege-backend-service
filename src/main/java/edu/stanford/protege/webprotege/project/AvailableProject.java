package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.user.UserId;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Mar 2017
 *
 * Represents information about a project that is available (viewable) for a given user.
 */
@JsonPropertyOrder({"projectId",
        "displayName",
        "description",
        "owner",
        "createdAt",
        "createdBy",
        "lastModifiedAt",
        "lastModifiedBy",
        "inTrash",
        "trashable",
        "downloadable"})
@AutoValue

public abstract class AvailableProject implements  HasProjectId, Comparable<AvailableProject> {

    public static final long UNKNOWN = 0;

    private static final Comparator<AvailableProject> COMPARATOR = Comparator.comparing((AvailableProject ap) -> ap.getDisplayName().toLowerCase());

    /**
     * Captures the information about a project that is available for the current
     * user.
     *
     * @param downloadable        A flag indicating whether the project is downloadable by the current
     *                            user (in the current session).
     * @param trashable           A flag indicating whether the project can be moved to the trash by
     * @param lastOpenedTimestamp A time stamp of when the project was last opened by the current
     *                            user.  A zero or negative value indicates unknown.
     */
    @JsonCreator
    public static AvailableProject get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                       @JsonProperty("displayName") @Nonnull String displayName,
                                       @JsonProperty("description") @Nonnull String description,
                                       @JsonProperty("owner") @Nonnull UserId owner,
                                       @JsonProperty("inTrash") boolean inTrash,
                                       @JsonProperty("createdAt") long createdAt,
                                       @JsonProperty("createdBy") @Nonnull UserId createdBy,
                                       @JsonProperty("lastModifiedAt") long lastModifiedAt,
                                       @JsonProperty("lastModifiedBy") @Nonnull UserId lastModifiedBy,
                                       @JsonProperty("downloadable") boolean downloadable,
                                       @JsonProperty("trashable") boolean trashable,
                                       @JsonProperty("lastOpenedAt") long lastOpenedTimestamp) {
        return new AutoValue_AvailableProject(projectId,
                                              displayName,
                                              owner,
                                              description,
                                              inTrash,
                                              createdAt,
                                              createdBy,
                                              lastModifiedAt,
                                              lastModifiedBy,
                                              downloadable,
                                              trashable,
                                              lastOpenedTimestamp);
    }

    /**
     * Gets the {@link ProjectId}
     *
     * @return The {@link ProjectId}
     */
    @Override
    @Nonnull
    public abstract ProjectId getProjectId();


    /**
     * Gets the display name of the project.
     *
     * @return A string representing the display name.
     */
    @Nonnull
    public abstract String getDisplayName();


    /**
     * Gets the owner of the project.
     *
     * @return The owner of the project represented by a {@link UserId}.
     */
    @Nonnull
    public abstract UserId getOwner();

    /**
     * Gets the project description.
     *
     * @return A possibly empty string representing the project description.
     */
    @Nonnull
    public abstract String getDescription();

    /**
     * Determines whether this project is in the trash or not.
     *
     * @return true if the project is in the trash, otherwise false.
     */
    public abstract boolean isInTrash();

    /**
     * Gets the timestamp of when the project was created.
     *
     * @return A long representing the timestamp.
     */
    public abstract long getCreatedAt();

    /**
     * Get the user who created the project.
     *
     * @return A {@link UserId} representing the user who created the project.
     */
    @Nonnull
    public abstract UserId getCreatedBy();

    /**
     * Gets the timestamp of when the project was last modified.
     *
     * @return A long representing a timestamp.
     */
    public abstract long getLastModifiedAt();

    /**
     * Get the id of the user who last modified the project.
     *
     * @return A {@link UserId} identifying the user who last modified the project.
     */
    @Nonnull
    public abstract UserId getLastModifiedBy();

    /**
     * Determines if this project is downloadable (by the current user).
     *
     * @return true if the project is downloadable, otherwise false.
     */
    public abstract boolean isDownloadable();


    /**
     * Determines if this project is trashable (by the current user).
     *
     * @return true if the project is trashable, otherwise false.
     */
    public abstract boolean isTrashable();

    /**
     * Gets the timestamp of when the project was last opened by the current user.
     *
     * @return The timestamp.  A value of 0 or a negative value indicated unknown.
     */
    public abstract long getLastOpenedAt();

    @Override
    public int compareTo(AvailableProject o) {
        return COMPARATOR.compare(this, o);
    }
}
