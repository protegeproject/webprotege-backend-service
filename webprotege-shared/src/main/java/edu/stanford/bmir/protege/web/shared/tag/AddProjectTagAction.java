package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
@JsonTypeName("AddProjectTag")
@AutoValue
@GwtCompatible(serializable = true)
public abstract class AddProjectTagAction implements ProjectAction<AddProjectTagResult> {

    /**
     * Creates an {@link AddProjectTagAction}.
     * @param projectId The project id which the tag should be added to.
     * @param label The label for the tag.  Must not be empty.
     * @param description The description for the tag.  May be empty.
     * @param color The color for the tag (foreground).
     * @param backgroundColor The background-color for the tag
     */
    @JsonCreator
    @Nonnull
    public static AddProjectTagAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("label") @Nonnull String label,
                                                    @JsonProperty("description") @Nonnull String description,
                                                    @JsonProperty("color") @Nonnull Color color,
                                                    @JsonProperty("backgroundColor") @Nonnull Color backgroundColor) {
        return new AutoValue_AddProjectTagAction(projectId,
                                                 label,
                                                 description,
                                       color, backgroundColor);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract String getLabel();

    @Nonnull
    public abstract String getDescription();

    @Nonnull
    public abstract Color getColor();

    @Nonnull
    public abstract Color getBackgroundColor();
}
