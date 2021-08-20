package edu.stanford.protege.webprotege.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

@AutoValue

@JsonTypeName("GetAllOntologies")
public abstract class GetUploadedAndProjectOntologyIdsResult implements Result {

    @JsonCreator
    public static GetUploadedAndProjectOntologyIdsResult create(@JsonProperty("ontologies") List<OWLOntologyID> ontologies) {
        return new AutoValue_GetUploadedAndProjectOntologyIdsResult(ontologies);
    }

    public abstract List<OWLOntologyID> getOntologies();
}
