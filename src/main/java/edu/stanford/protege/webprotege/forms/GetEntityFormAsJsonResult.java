package edu.stanford.protege.webprotege.forms;

import com.fasterxml.jackson.databind.JsonNode;
import edu.stanford.protege.webprotege.common.Response;

public record GetEntityFormAsJsonResult(JsonNode form) implements Response {
}
