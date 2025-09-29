package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectEvent;

import javax.annotation.Nonnull;

public sealed interface ProjectHistoryReplacementEvent extends ProjectEvent
    permits ProjectHistoryReplacementCompletionEvent, ProjectHistoryReplacementStartedEvent {

    @Nonnull
    ProjectHistoryReplacementOperationId operationId();
}
