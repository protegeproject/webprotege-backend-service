package edu.stanford.protege.webprotege.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
@AutoValue

@JsonTypeName("CopyFormDescriptors")
public abstract class CopyFormDescriptorsAction implements ProjectAction<CopyFormDescriptorsResult> {

    public static final String CHANNEL = "webprotege.forms.CopyFormDescriptors";

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract ProjectId getToProjectId();

    @Nonnull
    public abstract ImmutableList<FormId> getFormIds();

    @JsonCreator
    public static CopyFormDescriptorsAction create(@JsonProperty("projectId") ProjectId projectId,
                                                   @JsonProperty("toProjectId") ProjectId toProjectId,
                                                   @JsonProperty("formIds") ImmutableList<FormId> formIds) {
        return new AutoValue_CopyFormDescriptorsAction(projectId,
                                                       toProjectId,
                                                       formIds);
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
