package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;
import org.semanticweb.owlapi.model.IRI;

import java.util.Set;

@JsonTypeName(CheckNonExistentIrisAction.CHANNEL)
public record CheckNonExistentIrisResult(
        @JsonProperty("nonExistentIris") Set<IRI> nonExistentIris
) implements Result {

    public static CheckNonExistentIrisResult create(Set<IRI> nonExistentIris) {
        return new CheckNonExistentIrisResult(nonExistentIris);
    }
}
