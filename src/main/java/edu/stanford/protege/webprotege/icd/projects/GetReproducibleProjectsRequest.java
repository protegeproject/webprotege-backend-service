package edu.stanford.protege.webprotege.icd.projects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Request;

import static edu.stanford.protege.webprotege.icd.projects.GetReproducibleProjectsRequest.CHANNEL;

@JsonTypeName(CHANNEL)

public record GetReproducibleProjectsRequest() implements Request<GetReproducibleProjectsResponse> {
    public static final String CHANNEL = "icatx.versioning.GetReproducibleProjects";


    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
