package edu.stanford.protege.webprotege.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nonnull;

import static edu.stanford.protege.webprotege.project.CreateBackupOwlFileAction.CHANNEL;

@JsonTypeName(CHANNEL)

public record CreateBackupOwlFileAction(@JsonProperty("projectId") ProjectId projectId) implements ProjectAction<CreateBackupOwlFileResponse>, HasProjectId {
    public final static String CHANNEL = "webprotege.projects.CreateBackupOwlFile";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
    @Nonnull
    @Override
    public ProjectId projectId() {
        return projectId;
    }
}
