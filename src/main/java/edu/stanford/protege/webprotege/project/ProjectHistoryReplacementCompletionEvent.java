package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.ProjectEvent;

public sealed interface ProjectHistoryReplacementCompletionEvent extends ProjectHistoryReplacementEvent permits ProjectHistoryReplacementSucceededEvent, ProjectHistoryReplacementFailedEvent {

}
