package edu.stanford.bmir.protege.web.server.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import edu.stanford.bmir.protege.web.server.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.server.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
@AutoValue

@JsonTypeName("SetProjectTags")
public abstract class SetProjectTagsAction implements ProjectAction<SetProjectTagsResult> {

    @JsonCreator
    public static SetProjectTagsAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                              @JsonProperty("tagData") @Nonnull List<TagData> tagData) {
        return new AutoValue_SetProjectTagsAction(projectId, tagData);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract List<TagData> getTagData();
}
