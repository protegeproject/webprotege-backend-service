package edu.stanford.protege.webprotege.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.List;

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
