package edu.stanford.protege.webprotege.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.protege.webprotege.dispatch.Result;

import java.util.List;

public record GetIcatxEntityTypeResult(@JsonProperty(value = "icatxEntityTypes") List<String> icatxEntityTypes) implements Result {
}
