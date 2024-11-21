package edu.stanford.protege.webprotege.icd.actions;

import com.fasterxml.jackson.annotation.*;
import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;

import javax.annotation.Nullable;

import static edu.stanford.protege.webprotege.icd.actions.CreateClassesFromApiAction.CHANNEL;

@JsonTypeName(CHANNEL)
public record CreateClassesFromApiAction(@JsonProperty("changeRequestId") ChangeRequestId changeRequestId,
                                         @JsonProperty("projectId") ProjectId projectId,
                                         @JsonProperty("sourceText") String sourceText,
                                         @JsonProperty("langTag") @Nullable String langTag,
                                         @JsonProperty("parent") ImmutableSet<String> parent) implements ProjectAction<CreateClassesFromApiResult> {
    public static final String CHANNEL = "icatx.webprotege.entities.CreateClassesFromApi";

    public String getChannel() {
        return CHANNEL;
    }
}
