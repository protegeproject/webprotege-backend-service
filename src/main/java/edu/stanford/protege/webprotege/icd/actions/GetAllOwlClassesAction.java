package edu.stanford.protege.webprotege.icd.actions;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;

public record GetAllOwlClassesAction(ProjectId projectId) implements ProjectRequest<GetAllOwlClassesResult> {
    public final static String CHANNEL = "webprotege.entities.GetAllOwlClasses";


    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public ProjectId projectId(){
        return projectId;
    }
}
