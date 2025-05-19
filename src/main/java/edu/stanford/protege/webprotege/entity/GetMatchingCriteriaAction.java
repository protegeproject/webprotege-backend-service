package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.criteria.CompositeRootCriteria;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import org.semanticweb.owlapi.model.IRI;

import java.util.List;
import java.util.Map;


@JsonTypeName(GetMatchingCriteriaAction.CHANNEL)
public record GetMatchingCriteriaAction(@JsonProperty("criteriaMap") Map<String, List<CompositeRootCriteria>> criteriaMap,
                                        @JsonProperty("projectId") ProjectId projectId,
                                        @JsonProperty("entityIri") IRI entitiyIri) implements ProjectAction<GetMatchingCriteriaResult> {

    public static final String CHANNEL = "webprotege.entities.GetMatchingCriteria";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
