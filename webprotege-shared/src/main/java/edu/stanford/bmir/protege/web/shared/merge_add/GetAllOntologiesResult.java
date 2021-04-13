package edu.stanford.bmir.protege.web.shared.merge_add;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetAllOntologies")
public abstract class GetAllOntologiesResult implements Result {

    @JsonCreator
    public static GetAllOntologiesResult create(@JsonProperty("ontologies") List<OWLOntologyID> ontologies) {
        return new AutoValue_GetAllOntologiesResult(ontologies);
    }

    public abstract List<OWLOntologyID> getOntologies();
}
