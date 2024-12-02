package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.dispatch.Action;

public record GetAllOwlClassesAction(ProjectId projectId) implements Action<GetAllOwlClassesResult> {
    public final static String CHANNEL = "webprotege.entities.GetAllOwlClasses";


    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
