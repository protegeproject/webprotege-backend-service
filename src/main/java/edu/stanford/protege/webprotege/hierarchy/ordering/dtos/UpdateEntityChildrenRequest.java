package edu.stanford.protege.webprotege.hierarchy.ordering.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.Request;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;

@JsonTypeName(UpdateEntityChildrenRequest.CHANNEL)
public record UpdateEntityChildrenRequest(  @JsonProperty("projectId") ProjectId projectId,
                                            @JsonProperty("entityIri") IRI entityIri,
                                            @JsonProperty("childrenIris") List<String> childrenIris) implements Request<UpdateEntityChildrenResponse> {

    public final static String CHANNEL =  "icatx.versioning.UpdateEntityChildren";


    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
