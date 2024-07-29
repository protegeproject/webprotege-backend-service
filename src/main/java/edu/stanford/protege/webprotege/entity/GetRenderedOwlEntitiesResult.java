package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;


@JsonTypeName("webprotege.entities.RenderedOwlEntities")
public record GetRenderedOwlEntitiesResult(@JsonProperty List<EntityNode> renderedEntities) implements Result {

}
